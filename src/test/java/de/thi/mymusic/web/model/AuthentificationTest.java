package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.User;
import de.thi.mymusic.fixture.UserFixture;
import de.thi.mymusic.service.UserService;
import de.thi.mymusic.util.GuiUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.Writer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Michael on 11.12.2015.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class, UIViewRoot.class, ExternalContext.class, Writer.class, HttpServletRequest.class, GuiUtils.class})
public class AuthentificationTest {

    /**
     * class under test
     */
    Authentification authentification;

    UserService mockedUserService;
    @Mock
    FacesContext mockedFacesContext;
    @Mock
    UIViewRoot mockedUIViewRoot;
    @Mock
    ExternalContext mockedExternalContext;
    @Mock
    Writer mockedWriter;
    @Mock
    HttpServletRequest mockedHttpServletRequest;
    @Mock
    GuiUtils mockedGuiUtils;

    @Before
    public void setUp() throws Exception {
        mockedUserService = mock(UserService.class);
        authentification = new Authentification(mockedUserService);
        PowerMockito.mockStatic(FacesContext.class);
        PowerMockito.mockStatic(UIViewRoot.class);
        PowerMockito.mockStatic(ExternalContext.class);
        PowerMockito.mockStatic(HttpServletRequest.class);
        PowerMockito.mockStatic(GuiUtils.class);
        when(FacesContext.getCurrentInstance()).thenReturn(mockedFacesContext);
        when(mockedFacesContext.getExternalContext()).thenReturn(mockedExternalContext);
        when(mockedExternalContext.getRequest()).thenReturn(mockedHttpServletRequest);
        when(mockedFacesContext.getViewRoot()).thenReturn(mockedUIViewRoot);
    }

    /**
     * method under test: doLogin
     */

    @Test
    public void ThatDoLoginReturnNullIfWasCorrect() throws Exception {
        User testUser = UserFixture.aUser();
        authentification.setUsername(testUser.getUsername());
        authentification.setPassword(testUser.getPassword());
        when(mockedUIViewRoot.getViewId()).thenReturn("search.xhtml");

        String viewResult = authentification.doLogin();

        verify(mockedHttpServletRequest).login(testUser.getUsername(), testUser.getPassword());
        assertEquals(null, viewResult);
    }

    @Test
    public void ThatDoLoginReturnSearchPageIfWasCorrectAndViewIdWasLogin() throws Exception {
        when(mockedUIViewRoot.getViewId()).thenReturn("/login.xhtml");

        String viewResult = authentification.doLogin();

        assertEquals("/search.xhtml", viewResult);
    }

    @Test
    public void ThatDoLoginReturnSearchPageIfWasCorrectAndViewIdWasLoginErrorPage() throws Exception {
        when(mockedUIViewRoot.getViewId()).thenReturn("/loginerror.xhtml");

        String viewResult = authentification.doLogin();

        assertEquals("/search.xhtml", viewResult);
    }

    @Test
    public void ThatDoLoginReturnErrorPageIfWasIncorrect() throws Exception {
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
    public void ThatDoLogoutWriteResponseCorrect() throws Exception {
        PowerMockito.mockStatic(Writer.class);
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
    public void ThatCheckNavigationLoginDisableIsTrueAtLoginPage() throws Exception {
        when(mockedUIViewRoot.getViewId()).thenReturn("/login.xhtml");

        boolean result = authentification.checkNavigationLoginDisable();

        assertEquals(true, result);
    }

    @Test
    public void ThatCheckNavigationLoginDisableIsFalseAtAnyPageThenLoginPage() throws Exception {
        when(mockedUIViewRoot.getViewId()).thenReturn("/search.xhtml");

        boolean result = authentification.checkNavigationLoginDisable();

        assertEquals(false, result);
    }

}