package com.google.gwt.user.client.ui;

import com.github.perfectscrollbar.client.PerfectScrollbarPanel;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

public class ScrollPanel extends PerfectScrollbarPanel implements HasOneWidget, ProvidesResize, RequiresResize
{
   public ScrollPanel()
   {
      super();
   }

   public ScrollPanel(Widget w)
   {
      super();
      setWidget(w);
   }

   @Override
   public void add(Widget w)
   {
      // Can't add() more than one widget to a SimplePanel.
      if (getWidget() != null)
      {
         throw new IllegalStateException("userdef ScrollPanel can only contain one child widget. Use PerfectScrollbarPanel instead.");
      }
      setWidget(w);
   }

   @Override
   public void setWidget(Widget w)
   {
      if (this.getWidgetCount() > 0)
      {
         this.clear();
      }
      super.add(w);
   }

   @Override
   public void onResize()
   {
      for (Widget child : getChildren())
      {
         if (child instanceof RequiresResize)
         {
            ((RequiresResize) child).onResize();
         }
      }
      this.update();
   }

   @Override
   public com.google.gwt.user.client.Element getElement()
   {
      return super.getElement();
   }

   @Override
   public Widget getWidget()
   {
      return this.getWidgetCount() == 0 ? null : this.getWidget(0);
   }

   @Override
   public void setWidget(IsWidget w)
   {
      this.setWidget(w.asWidget());
   }

   public void setAlwaysShowScrollBars(boolean set)
   {
      setShowBars(set ? ShowBars.ALWAYS : ShowBars.ONHOVER);
   }

   protected com.google.gwt.user.client.Element getScrollableElement()
   {
      return DOM.asOld(getWidget().getElement());
   }

   protected com.google.gwt.user.client.Element getContainerElement()
   {
      return DOM.asOld(this.getElement());
   }

}
