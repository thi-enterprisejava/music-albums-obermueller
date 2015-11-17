package de.thi.mymusic.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;


public class GuiUtil {

    /**
     *
     * Translation of FacesMessages
     *
     * @param context: FacesContext
     * @param severity: Severity Level of FacesMessage
     * @param msgKey: Key of property
     * @param args: Optional arguements as message input
     * @return: Translated FacesMessage
     */
    public static FacesMessage getFacesMessage(FacesContext context, FacesMessage.Severity severity,
            String msgKey, Object... args) {
        Locale loc = context.getViewRoot().getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(context.getApplication().getMessageBundle(), loc);
        String msg = bundle.getString(msgKey);

        if (args != null) {
            MessageFormat format = new MessageFormat(msg);
            msg = format.format(args);
        }

        return new FacesMessage(severity, msg, null);
    }

    public static String getCurrentNavigationClass(String navigationElement) {

        String result = "current";
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        if("search".equals(navigationElement)) {
            if("/search.xhtml".equals(viewId) || "/listSearchResult.xhtml".equals(viewId)) {
                return result;
            }
        } else if("add".equals(navigationElement)) {
            if("/add.xhtml".equals(viewId)) {
                return result;
            }
        }

        return "";
    }
}
