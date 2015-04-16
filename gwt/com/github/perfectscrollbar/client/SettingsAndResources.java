package com.github.perfectscrollbar.client;

import com.github.gwtbootstrap.client.ui.resources.JavaScriptInjector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public class SettingsAndResources extends Settings
{
   public interface Res extends ClientBundle
   {
      public static Res RESOURCE = GWT.create(Res.class);

      @Source("perfect-scrollbar.min.js")
      TextResource javascript();

      @Source("perfect-scrollbar.css")
      TextResource css();
   }

   static
   {
      Res r = Res.RESOURCE;
      StyleInjector.inject(r.css().getText());
      inject(r.javascript().getText());
   }

   public static void inject(String javascript)
   {
      Element element = Document.get().getElementsByTagName("head").getItem(0);
      assert element != null : "HTML Head element required";
      HeadElement head = HeadElement.as(element);
      ScriptElement selement = createScriptElement();
      selement.setText(javascript);
      head.appendChild(selement);
   }

   private static ScriptElement createScriptElement()
   {
      ScriptElement script = Document.get().createScriptElement();
      script.setAttribute("type", "text/javascript");
      script.setAttribute("charset", "UTF-8");
      return script;
   }

}
