package de.thi.mymusic.util;

import de.thi.mymusic.datapoint.NavigationDataPoint;
import de.thi.mymusic.mocker.ContextMocker;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Michael on 31.12.2015.
 */

@RunWith(Theories.class)
public class GuiUtilsTest {

    /**
     * class under test
     */
    GuiUtils guiUtils;

    FacesContext mockedFacesContext;
    UIViewRoot mockedUIViewRoot;
    Application mockedApplication;


    @DataPoints
    public static NavigationDataPoint[] navigationDataPoints = {
            new NavigationDataPoint("/search.xhtml", "search"),
            new NavigationDataPoint("/listSearchResult.xhtml", "search"),
            new NavigationDataPoint("/detailAlbum.xhtml", "search"),
            new NavigationDataPoint("/edit.xhtml", "edit"),
            new NavigationDataPoint("/editUser.xhtml", "userManagement"),
            new NavigationDataPoint("/changePassword.xhtml", "changePassword")
        };

    @Before
    public void setUp() throws Exception {
        mockedFacesContext = ContextMocker.mockFacesContext();
        mockedUIViewRoot = mock(UIViewRoot.class);
        when(mockedFacesContext.getViewRoot()).thenReturn(mockedUIViewRoot);
    }


    /**
     * method under test: getCurrentNavigationClass
     */
    @Theory
    public void thatGetCurrentNavigationClassReturnCurrentIfViewIdIsNavigationElement(NavigationDataPoint navigationDataPoint) throws Exception {
        when(mockedUIViewRoot.getViewId()).thenReturn(navigationDataPoint.getViewName());

        String result = GuiUtils.getCurrentNavigationClass(navigationDataPoint.getNavigationName());

        assertEquals("current", result);
    }

    @Test
    public void thatGetCurrentNavigationClassReturnNothingIfViewIdIsNotNavigationElement() throws Exception {
        when(mockedUIViewRoot.getViewId()).thenReturn("/edit.xhtml");

        String result = GuiUtils.getCurrentNavigationClass("search");

        assertEquals("", result);
    }

    @Test
    public void thatGetCurrentNavigationClassReturnNothingIfViewIdIsNotKnown() throws Exception {
        when(mockedUIViewRoot.getViewId()).thenReturn("/editTest.xhtml");

        String result = GuiUtils.getCurrentNavigationClass("search");

        assertEquals("", result);
    }

    /**
     * method under test: getFacesMessage
     */

    @Test
    public void thatGetFacesMessageReturnTranslatedMessage() throws Exception {
        when(mockedUIViewRoot.getLocale()).thenReturn(new Locale("de"));
        mockedApplication = mock(Application.class);
        when(mockedFacesContext.getApplication()).thenReturn(mockedApplication);
        when(mockedApplication.getMessageBundle()).thenReturn("test_messages");
        guiUtils = new GuiUtils();
        FacesMessage expectedMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Testwert", null);

        FacesMessage message = guiUtils.getFacesMessage(mockedFacesContext,
                FacesMessage.SEVERITY_INFO, "test", null);

        assertNotNull(message);
        assertEquals(expectedMessage.getSummary(), message.getSummary());
        assertEquals(expectedMessage.getSeverity(), message.getSeverity());
    }

    @Test
    public void thatGetFacesMessageReturnTranslatedMessageWithArguments() throws Exception {
        when(mockedUIViewRoot.getLocale()).thenReturn(new Locale("de"));
        mockedApplication = mock(Application.class);
        when(mockedFacesContext.getApplication()).thenReturn(mockedApplication);
        when(mockedApplication.getMessageBundle()).thenReturn("test_messages");
        guiUtils = new GuiUtils();
        FacesMessage expectedMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Testwert: value", null);

        FacesMessage message = guiUtils.getFacesMessage(mockedFacesContext,
                FacesMessage.SEVERITY_INFO, "test.value", new Object[] {"value"});

        assertNotNull(message);
        assertEquals(expectedMessage.getSummary(), message.getSummary());
        assertEquals(expectedMessage.getSeverity(), message.getSeverity());
    }
}