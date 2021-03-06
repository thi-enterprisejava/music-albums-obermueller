package de.thi.mymusic.util;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

@Stateless
@PermitAll
public class GuiUtils {

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
    public FacesMessage getFacesMessage(FacesContext context, FacesMessage.Severity severity,
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

    /**
     * Checks ViewId to style current navigation element
     * @param navigationElement
     * @return <code>current</code> if and only if the viewId is in group of navigationElement
     *         <code></code> otherwise
     */
    public static String getCurrentNavigationClass(String navigationElement) {
        String result = "current";
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        if("search".equals(navigationElement) && ("/search.xhtml".equals(viewId)
                || "/listSearchResult.xhtml".equals(viewId) || "/detailAlbum.xhtml".equals(viewId))) {
            return result;
        } else if("edit".equals(navigationElement) && "/edit.xhtml".equals(viewId)) {
            return result;
        } else if("userManagement".equals(navigationElement) && "/editUser.xhtml".equals(viewId)) {
            return result;
        } else if("changePassword".equals(navigationElement) && "/changePassword.xhtml".equals(viewId)) {
            return result;
        }

        return "";
    }
}
