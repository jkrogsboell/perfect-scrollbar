package com.github.perfectscrollbar.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasScrolling;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class PerfectScrollbarPanel extends FlowPanel implements HasScrolling //ScrollPanel
{
   Settings settings = GWT.create(Settings.class);

   public enum ShowBars
   {
      ALWAYS, ONHOVER
   };

   public PerfectScrollbarPanel()
   {
      super();
      this.getElement().getStyle().setPosition(Position.RELATIVE);

      setShowBars(settings.defaultShowBars);

      addMSOverflow(getElement()); //touchscreen Windows 8.1 - internet explorer 11
   }

   private int top_cache = -1;
   private int left_cache = -1;

   private native void addMSOverflow(Element element) /*-{
      if ('msOverflowStyle' in document.body.style) {
         element.style.cssText = element.style.cssText + "; overflow: auto !important; -ms-overflow-style : none !important";
         //         $wnd.jQuery(element).css({
         //            "cssText" : "overflow: auto !important; -ms-overflow-style : none !important"
         //         });
      }
   }-*/;

   public void setShowBars(ShowBars set)
   {
      if (set == ShowBars.ALWAYS)
      {
         this.addStyleName("always-visible");
      }
      else
      {
         this.removeStyleName("always-visible");
      }
   }

   @Override
   public void onAttach()
   {
      super.onAttach();

      Scheduler.get().scheduleFinally(new ScheduledCommand()
      {
         @Override
         public void execute()
         {
            PerfectScrollbarPanel that = PerfectScrollbarPanel.this;
            that.initalizePerfectScrollbar(that.getElement());
            initialized = true;

            if (top_cache != -1 && left_cache != -1)
            {
               that.scrollTopLeftJSNI(getElement(), top_cache, left_cache);
            }
            else if (top_cache != -1)
            {
               that.scrollTopJSNI(getElement(), top_cache);
            }
            else if (left_cache != -1)
            {
               that.scrollLeftJSNI(getElement(), left_cache);
            }

            top_cache = -1;
            left_cache = -1;
         }
      });
   }

   private boolean initialized = false;

   private native void initalizePerfectScrollbar(Element element) /*-{
      $wnd.Ps.initialize(element, {
      //"useKeyboard" : false
      });
   }-*/;

   @Override
   public void onDetach()
   {
      super.onDetach();
      //chrome 42 error on reattach //chrome 44 works just fine
//    initialized = false;
//    destroy(getElement());
   }

   @Override
   public void add(Widget w)
   {
      if (initialized)
      {

         // Detach new child.
         w.removeFromParent();

         // Logical attach.
         getChildren().add(w);

         // Physical attach.
         //DOM.appendChild(container, child.getElement());
         int insertAt = getChildren().size() - 1;

//         GWT.log("PSB insertAt: " + insertAt);
         DOM.insertChild(getElement(), w.getElement(), insertAt); //2 rails after

         // Adopt.
         adopt(w);
      }
      else
      {
         super.add(w);
      }
   }

   @Override
   public void add(IsWidget w)
   {
      this.add(w.asWidget());
   }

   private native void destroy(Element element) /*-{
      $wnd.Ps.destroy(element);
   }-*/;

   public void update()
   {
      if (initialized)
         updateJSNI(getElement());
   }

   private native void updateJSNI(Element element) /*-{
      $wnd.Ps.update(element);
   }-*/;

   @Override
   public void setVerticalScrollPosition(int top)
   {
      if (initialized)
         scrollTopJSNI(getElement(), top);
      else
         top_cache = top;
   }

   private native void scrollTopJSNI(Element element, int top) /*-{
      element.scrollTop = top;
      $wnd.Ps.update(element);
   }-*/;

   @Override
   public void setHorizontalScrollPosition(int left)
   {
      if (initialized)
         scrollLeftJSNI(getElement(), left);
      else
         left_cache = left;
   }

   private native void scrollLeftJSNI(Element element, int left) /*-{
      element.scrollLeft = left;
      $wnd.Ps.update(element);
   }-*/;

   public void scrollTo(int top, int left)
   {
      if (initialized)
         scrollTopLeftJSNI(getElement(), top, left);
      else
      {
         top_cache = top;
         left_cache = left;
      }
   }

   private native void scrollTopLeftJSNI(Element element, int top, int left) /*-{
      element.scrollTop = top;
      element.scrollLeft = left;
      $wnd.Ps.update(element);
   }-*/;

   @Override
   public int getVerticalScrollPosition()
   {
      return getElement().getScrollTop();
   }

   @Override
   public int getHorizontalScrollPosition()
   {
      return getElement().getScrollLeft();
   }

   @Override
   public HandlerRegistration addScrollHandler(ScrollHandler handler)
   {
      return addDomHandler(handler, ScrollEvent.getType());
   }

   @Override
   public int getMaximumHorizontalScrollPosition()
   {
      int maxChildWidth = 0;
      for (Widget child : this.getChildren())
      {
         if (child.getOffsetWidth() > maxChildWidth)
            maxChildWidth = child.getOffsetWidth();
      }

      if (maxChildWidth > this.getOffsetWidth())
         return 0;
      return maxChildWidth - this.getOffsetWidth();
   }

   @Override
   public int getMinimumHorizontalScrollPosition()
   {
      return 0; // no rtl support right now
   }

   @Override
   public int getMaximumVerticalScrollPosition()
   {
      int maxChildHeight = 0;
      for (Widget child : this.getChildren())
      {
         if (child.getOffsetWidth() > maxChildHeight)
            maxChildHeight = child.getOffsetHeight();
      }

      if (maxChildHeight > this.getOffsetHeight())
         return 0;
      return maxChildHeight - this.getOffsetHeight();
   }

   @Override
   public int getMinimumVerticalScrollPosition()
   {
      return 0; // no rtl support right now
   }

   //do not remove all dom children!
   @Override
   public void clear()
   {
      for (Widget w : this.getChildren())
      {
         this.remove(w);
      }

   }

}
