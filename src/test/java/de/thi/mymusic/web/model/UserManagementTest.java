package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.User;
import de.thi.mymusic.fixture.UserFixture;
import de.thi.mymusic.service.UserService;
import de.thi.mymusic.util.GuiUtils;
import org.junit.Before;
import org.junit.Test;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import sun.security.acl.PrincipalImpl;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 11.12.2015.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class, ExternalContext.class, HttpServletRequest.class, GuiUtils.class})
public class UserManagementTest {

    /**
     * class under test
     */
    UserManagement userManagement;

    UserService mockedUserService;
    @Mock
    FacesContext mockedFacesContext;
    @Mock
    ExternalContext mockedExternalContext;
    @Mock
    HttpServletRequest mockedHttpServletRequest;
    UIComponent mockedUIComponent;
    Object mockedValue;
    @Mock
    GuiUtils mockedGuiUtils;

    @Before
    public void setUp() throws Exception {
        mockedUserService = mock(UserService.class);
        userManagement = new UserManagement(mockedUserService);
        PowerMockito.mockStatic(FacesContext.class);
    }

    /**
     * method under test: init
     */

    @Test
    public void ThatInitSetFoundUserCorrect() throws Exception {
        userManagement.setUserId(1L);
        User testUser = UserFixture.aUser();
        when(mockedUserService.findById(1L)).thenReturn(testUser);

        String viewResult = userManagement.init();

        User user = userManagement.getUser();
        assertNotNull(user);
        assertEquals(testUser, user);
        assertNull(viewResult);
        verify(mockedUserService).findById(1L);
    }

    @Test
    public void ThatInitIncorrectUserIdReturnErrorPageAndAddErrorMessage() throws Exception {
        userManagement.setUserId(2L);
        when(mockedUserService.findById(2L)).thenReturn(null);
        when(FacesContext.getCurrentInstance()).thenReturn(mockedFacesContext);
        ArgumentCaptor<FacesMessage> facesMessageCaptor = ArgumentCaptor
                .forClass(FacesMessage.class);

        String viewResult = userManagement.init();

        User user = userManagement.getUser();
        assertNull(user);
        assertNotNull(viewResult);
        assertEquals("error", viewResult);
        verify(mockedUserService).findById(2L);
        verify(mockedFacesContext).addMessage(any(),
                facesMessageCaptor.capture());
        FacesMessage message = facesMessageCaptor.getValue();
        assertEquals(FacesMessage.SEVERITY_ERROR, message.getSeverity());
    }

    @Test
    public void ThatInitNothingIsDoingIfUserIdIs0() throws Exception {
        userManagement.setUserId(0L);
        when(mockedUserService.findById(0L)).thenReturn(UserFixture.aUser());

        String viewResult = userManagement.init();

        User user = userManagement.getUser();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertNull(viewResult);
    }

    /**
     * method under test: initForPasswordChange
     */

    @Test
    public void ThatInitForPasswordSetCorrectUser() throws Exception {
        User testUser = UserFixture.aUser();
        PowerMockito.mockStatic(ExternalContext.class);
        PowerMockito.mockStatic(HttpServletRequest.class);
        when(mockedFacesContext.getCurrentInstance()).thenReturn(mockedFacesContext);
        when(mockedFacesContext.getExternalContext()).thenReturn(mockedExternalContext);
        when(mockedExternalContext.getRequest()).thenReturn(mockedHttpServletRequest);
        Principal principalMichael = new PrincipalImpl("Michael");
        when(mockedHttpServletRequest.getUserPrincipal()).thenReturn(principalMichael);
        when(mockedUserService.findByUsername("Michael")).thenReturn(testUser);

        String viewResult = userManagement.initForPasswordChange();

        User user = userManagement.getUser();
        assertNotNull(user);
        assertEquals(testUser, user);
        assertNull(viewResult);
        verify(mockedUserService).findByUsername("Michael");
    }

    @Test
    public void ThatInitForPasswordIncorrectUserReturnErrorPage() throws Exception {
        PowerMockito.mockStatic(ExternalContext.class);
        PowerMockito.mockStatic(HttpServletRequest.class);
        when(mockedFacesContext.getCurrentInstance()).thenReturn(mockedFacesContext);
        when(mockedFacesContext.getExternalContext()).thenReturn(mockedExternalContext);
        when(mockedExternalContext.getRequest()).thenReturn(mockedHttpServletRequest);
        Principal principalMichael = new PrincipalImpl("Michael2");
        when(mockedHttpServletRequest.getUserPrincipal()).thenReturn(principalMichael);
        when(mockedUserService.findByUsername("Michael2")).thenReturn(null);

        String viewResult = userManagement.initForPasswordChange();

        User user = userManagement.getUser();
        assertNull(user);
        assertEquals("/error.xhtml", viewResult);
    }

    @Test
    public void ThatInitForPasswordReturnErrorPageIfNotLoggedIn() throws Exception {
        PowerMockito.mockStatic(ExternalContext.class);
        PowerMockito.mockStatic(HttpServletRequest.class);
        when(mockedFacesContext.getCurrentInstance()).thenReturn(mockedFacesContext);
        when(mockedFacesContext.getExternalContext()).thenReturn(mockedExternalContext);
        when(mockedExternalContext.getRequest()).thenReturn(mockedHttpServletRequest);
        when(mockedHttpServletRequest.getUserPrincipal()).thenReturn(null);

        String viewResult = userManagement.initForPasswordChange();

        User user = userManagement.getUser();
        assertNotNull(user);
        assertEquals(0,user.getId());
        assertEquals("/error.xhtml", viewResult);
    }

    /**
     * method under test: validateUniqueUsername
     *
     */

    @Test
    public void ThatValidateUniqueUsernameThrowsExceptionIfUsernameIsNotUniqueAndOtherUserId() throws Exception {
        User testUser = UserFixture.aUser();
        mockedValue = mock(Object.class);
        mockedUIComponent = mock(UIComponent.class);
        when(mockedValue.toString()).thenReturn("Michael");
        Map<String, Object> mapAttributes = new HashMap<>();
        mapAttributes.put("userId", 2L);
        when(mockedUIComponent.getAttributes()).thenReturn(mapAttributes);
        when(mockedUserService.findByUsername("Michael")).thenReturn(testUser);
        PowerMockito.mockStatic(GuiUtils.class);
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "edit.user.usernameNotUnique")).thenReturn(new FacesMessage("edit.user.usernameNotUnique"));

        try {
            userManagement.validateUniqueUsername(mockedFacesContext, mockedUIComponent, mockedValue);

            fail("should throw a ValidatorException");
        } catch (ValidatorException ex) {

        }
    }

    @Test
    public void ThatValidateUniqueUsernameIsOkIfUsernameIsNotUniqueButSameUserId() throws Exception {
        User testUser = UserFixture.aUser();
        mockedValue = mock(Object.class);
        mockedUIComponent = mock(UIComponent.class);
        when(mockedValue.toString()).thenReturn("Michael");
        Map<String, Object> mapAttributes = new HashMap();
        mapAttributes.put("userId", 0L);
        when(mockedUIComponent.getAttributes()).thenReturn(mapAttributes);
        when(mockedUserService.findByUsername("Michael")).thenReturn(testUser);
        PowerMockito.mockStatic(GuiUtils.class);
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "edit.user.usernameNotUnique")).thenReturn(new FacesMessage("edit.user.usernameNotUnique"));

        try {
            userManagement.validateUniqueUsername(mockedFacesContext, mockedUIComponent, mockedValue);

        } catch (ValidatorException ex) {
            fail("shouldn´t throw a ValidatorException");
        }
    }

    @Test
    public void ThatValidateUniqueUsernameIsOkIfNoUserWithSameNameWasFound() throws Exception {
        mockedValue = mock(Object.class);
        mockedUIComponent = mock(UIComponent.class);
        when(mockedValue.toString()).thenReturn("Michael");
        Map<String, Object> mapAttributes = new HashMap();
        mapAttributes.put("userId", 0L);
        when(mockedUIComponent.getAttributes()).thenReturn(mapAttributes);
        when(mockedUserService.findByUsername("Michael")).thenReturn(null);
        PowerMockito.mockStatic(GuiUtils.class);
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "edit.user.usernameNotUnique")).thenReturn(new FacesMessage("edit.user.usernameNotUnique"));

        try {
            userManagement.validateUniqueUsername(mockedFacesContext, mockedUIComponent, mockedValue);

        } catch (ValidatorException ex) {
            fail("shouldn´t throw a ValidatorException");
        }
    }

    /**
     * method under test: doCancel
     */

    @Test
    public void ThatDoCancelReloadCorrectAtEditUser() {
        User testUser = UserFixture.aUser();
        String updatedUsername = "Michael2";
        userManagement.setUserId(2L);
        userManagement.getUser().setUsername(updatedUsername);
        when(mockedUserService.findById(2L)).thenReturn(testUser);

        String viewResult = userManagement.doCancel();

        assertEquals("editUser.xhtml?user=2", viewResult);
        assertEquals(testUser, userManagement.getUser());
    }

    @Test
    public void ThatDoCancelReloadCorrectAtCreateUser() {
        String insertedUsername = "Michael2";
        String insertedPassword = "huhu";
        userManagement.getUser().setUsername(insertedUsername);
        userManagement.getUser().setPassword(insertedPassword);

        String viewResult = userManagement.doCancel();

        assertNull(userManagement.getUser().getUsername());
        assertNull(userManagement.getUser().getPassword());
        assertEquals(0L, userManagement.getUser().getId());
        assertEquals("editUser.xhtml", viewResult);
    }
}