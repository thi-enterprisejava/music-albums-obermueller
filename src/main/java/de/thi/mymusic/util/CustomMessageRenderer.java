package de.thi.mymusic.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.MessageRenderer;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import javax.faces.render.FacesRenderer;



@FacesRenderer(componentFamily = "javax.faces.Message", rendererType = "javax.faces.Message")
public class CustomMessageRenderer extends MessageRenderer {
    private static final Attribute[] ATTRIBUTES = AttributeManager
            .getAttributes(AttributeManager.Key.MESSAGESMESSAGES);

    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        super.encodeBegin(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component))
            return;

        boolean mustRender = shouldWriteIdAttribute(component);

        UIMessage messages = (UIMessage) component;
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        String clientId = ((UIMessage) component).getFor();

        @SuppressWarnings("unchecked")
        Iterator<FacesMessage> messageIter = getMessageIter(context, clientId,
                component);

        assert (messageIter != null);

        if (!messageIter.hasNext()) {
            if (mustRender) {
                if ("javax_faces_developmentstage_messages".equals(component
                        .getId())) {
                    return;
                }
                writer.startElement("div", component);
                writeIdAttributeIfNecessary(context, writer, component);
                writer.endElement("div");
            }
            return;
        }

        writeIdAttributeIfNecessary(context, writer, component);

        // style is rendered as a passthru attribute
        RenderKitUtils.renderPassThruAttributes(context, writer, component,
                ATTRIBUTES);

        Map<Severity, List<FacesMessage>> msgs = new HashMap<Severity, List<FacesMessage>>();
        msgs.put(FacesMessage.SEVERITY_INFO, new ArrayList<FacesMessage>());
        // info
        msgs.put(FacesMessage.SEVERITY_WARN, new ArrayList<FacesMessage>());
        // warning
        msgs.put(FacesMessage.SEVERITY_ERROR, new ArrayList<FacesMessage>());
        // error
        msgs.put(FacesMessage.SEVERITY_FATAL, new ArrayList<FacesMessage>());
        // error

        while (messageIter.hasNext()) {
            FacesMessage curMessage = (FacesMessage) messageIter.next();

            if (curMessage.isRendered() && !messages.isRedisplay()) {
                continue;
            }
            msgs.get(curMessage.getSeverity()).add(curMessage);
        }

        List<FacesMessage> severityMessages = msgs
                .get(FacesMessage.SEVERITY_FATAL);
        if (severityMessages.size() > 0) {
            encodeSeverityMessages(context, component, messages,
                    FacesMessage.SEVERITY_FATAL, severityMessages);
        }

        severityMessages = msgs.get(FacesMessage.SEVERITY_ERROR);
        if (severityMessages.size() > 0) {
            encodeSeverityMessages(context, component, messages,
                    FacesMessage.SEVERITY_ERROR, severityMessages);
        }

        severityMessages = msgs.get(FacesMessage.SEVERITY_WARN);
        if (severityMessages.size() > 0) {
            encodeSeverityMessages(context, component, messages,
                    FacesMessage.SEVERITY_WARN, severityMessages);
        }

        severityMessages = msgs.get(FacesMessage.SEVERITY_INFO);
        if (severityMessages.size() > 0) {
            encodeSeverityMessages(context, component, messages,
                    FacesMessage.SEVERITY_INFO, severityMessages);
        }
    }

    private void encodeSeverityMessages(FacesContext facesContext,
                                        UIComponent component, UIMessage uiMessages, Severity severity,
                                        List<FacesMessage> messages) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        String alertSeverityClass = "";
        String iconServerityClass = "";

        if (FacesMessage.SEVERITY_INFO.equals(severity)) {
            alertSeverityClass = "success";
            iconServerityClass = "ok";
        } else if (FacesMessage.SEVERITY_WARN.equals(severity)) {
            alertSeverityClass = "warning"; // Default alert is a warning
            iconServerityClass = "warning";
        } else if (FacesMessage.SEVERITY_ERROR.equals(severity)) {
            alertSeverityClass = "error";
            iconServerityClass = "remove";
        } else if (FacesMessage.SEVERITY_FATAL.equals(severity)) {
            alertSeverityClass = "error";
            iconServerityClass = "remove";
        }

        writer.startElement("div", null);
        writer.writeAttribute("class", "notice " + alertSeverityClass, "notice "
                + alertSeverityClass);
        writer.startElement("i", null);
        writer.writeAttribute("class", "icon-" + iconServerityClass + " -sign icon-large", "icon-" + iconServerityClass + " -sign icon-large");
        writer.endElement("i");

        for (FacesMessage msg : messages) {
            String summary = msg.getSummary() != null ? msg.getSummary() : "";
            String detail = msg.getDetail() != null ? msg.getDetail() : summary;

            if (uiMessages.isShowSummary()) {
                writer.startElement("strong", component);
                writer.writeText(summary, component, null);
                writer.endElement("strong");
            }

            if (uiMessages.isShowDetail()) {
                writer.writeText(" " + detail, null);
            }
            msg.rendered();
        }
        writer.startElement("a", null);
        writer.writeAttribute("href", "#close", "#close");
        writer.writeAttribute("class", "icon-remove", "icon-remove");
        writer.endElement("a");
        writer.endElement("div");
    }
}
