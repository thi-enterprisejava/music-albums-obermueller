package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.User;
import de.thi.mymusic.fixture.UserFixture;
import de.thi.mymusic.mocker.ContextMocker;
import de.thi.mymusic.service.UserService;
import de.thi.mymusic.util.GuiUtils;
import org.junit.Before;
import org.junit.Test;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import java.io.Writer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Michael on 11.12.2015.
 */

public class AuthentificationTest {

    /**
     * class under test
     */
    Authentification authentification;

    FacesContext mockedFacesContext;
    UIViewRoot mockedUIViewRoot;
    ExternalContext mockedExternalContext;
    Writer mockedWriter;
    HttpServletRequest mockedHttpServletRequest;

    @Before
    public void setUp() throws Exception {
        authentification = new Authentification();
        mockedFacesContext = ContextMocker.mockFacesContext();
        mockedUIViewRoot = mock(UIViewRoot.class);
        mockedExternalContext = mock(ExternalContext.class);
        mockedHttpServletRequest = mock(HttpServletRequest.class);
        when(mockedFacesContext.getExternalContext()).thenReturn(mockedExternalContext);
        when(mockedExternalContext.getRequest()).thenReturn(mockedHttpServletRequest);
        when(mockedFacesContext.getViewRoot()).thenReturn(mockedUIViewRoot);
    }

    /**
     * method under test: doLogin
     */

    @Test
    public void thatDoLoginReturnNullIfWasCorrect() throws Exception {
        User testUser = UserFixture.aUser();
        authentification.setUsername(testUser.getUsername());
        authentification.setPassword(testUser.getPassword());
        when(mockedUIViewRoot.getViewId()).thenReturn("search.xhtml");

        String viewResult = authentification.doLogin();

        verify(mockedHttpServletRequest).login(testUser.getUsername(), testUser.getPassword());
        assertEquals(null, viewResult);
    }

    @Test
    public void thatDoLoginReturnSearchPageIfWasCorrectAndViewIdWasLogin() throws Exception {
        when(mockedUIViewRoot.getViewId()).thenReturn("/login.xhtml");

        String viewResult = authentification.doLogin();

        assertEquals("/search.xhtml", viewResult);
    }

    @Test
    public void thatDoLoginReturnSearchPageIfWasCorrectAndViewIdWasLoginErrorPage() throws Exception {
        when(mockedUIViewRoot.getViewId()).thenReturn("/loginerror.xhtml");

        String viewResult = authentification.doLogin();

        assertEquals("/search.xhtml", viewResult);
    }

    @Test
    public void thatDoLoginReturnErrorPageIfWasIncorrect() throws Exception {
        User testUser = UserFixture.aUser();
        authentification.setUsername(testUser.getUsername());
        authentification.setPassword(testUser.getPassword());
        // Throw Exception at login
        doThrow(new ServletException()).when(mockedHttpServletRequest).login(anyString(),anyString());

        String viewResult = authentification.doLogin();

        verify(mockedFacesContext).addMessage(anyString(), any(FacesMessage.class));
        verify(mockedHttpServletRequest).login(testUser.getUsername(), testUser.getPassword());
        assertEquals("/loginerror.xhtml", viewResult);
    }

    /**
     * method under test: doLogout
     */

    @Test
    public void thatDoLogoutWriteResponseCorrect() throws Exception {
        mockedWriter = mock(Writer.class);
        when(mockedExternalContext.getResponseOutputWriter()).thenReturn(mockedWriter);

        try{
            authentification.doLogout();

        } catch(Exception ex) {
            fail("Shouldn´t throw an exception");
        }
        verify(mockedExternalContext).invalidateSession();
        verify(mockedExternalContext).setResponseStatus(401);
        verify(mockedWriter).write("<html><head><meta http-equiv='refresh' content='0;search.xhtml'></head></html>");
    }

    /**
     * method under test: checkNavigationLoginDisable
     */

    @Test
    public void thatCheckNavigationLoginDisableIsTrueAtLoginPage() throws Exception {
        when(mockedUIViewRoot.getViewId()).thenReturn("/login.xhtml");

        boolean result = authentification.checkNavigationLoginDisable();

        assertEquals(true, result);
    }

    @Test
    public void thatCheckNavigationLoginDisableIsFalseAtAnyPageThenLoginPage() throws Exception {
        when(mockedUIViewRoot.getViewId()).thenReturn("/search.xhtml");

        boolean result = authentification.checkNavigationLoginDisable();

        assertEquals(false, result);
    }

}