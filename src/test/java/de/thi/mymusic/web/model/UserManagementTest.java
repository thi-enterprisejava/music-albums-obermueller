package de.thi.mymusic.web.model;

import de.thi.mymusic.domain.User;
import de.thi.mymusic.fixture.UserFixture;
import de.thi.mymusic.mocker.ContextMocker;
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

import org.mockito.ArgumentCaptor;
import sun.security.acl.PrincipalImpl;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 11.12.2015.
 */

public class UserManagementTest {

    /**
     * class under test
     */
    UserManagement userManagement;

    UserService mockedUserService;
    FacesContext mockedFacesContext;
    ExternalContext mockedExternalContext;
    HttpServletRequest mockedHttpServletRequest;
    UIComponent mockedUIComponent;
    Object mockedValue;
    GuiUtils mockedGuiUtils;

    @Before
    public void setUp() throws Exception {
        mockedUserService = mock(UserService.class);
        mockedGuiUtils = mock(GuiUtils.class);
        userManagement = new UserManagement(mockedUserService, mockedGuiUtils);
        mockedFacesContext  = ContextMocker.mockFacesContext();
    }

    /**
     * method under test: init
     */
    @Test
    public void thatInitSetFoundUserCorrect() throws Exception {
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
    public void thatInitIncorrectUserIdReturnErrorPageAndAddErrorMessage() throws Exception {
        userManagement.setUserId(2L);
        when(mockedUserService.findById(2L)).thenReturn(null);
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "user.usernameNotFound")).thenReturn(new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "user.usernameNotFound"));
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
    public void thatInitNothingIsDoingIfUserIdIs0() throws Exception {
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
    public void thatInitForPasswordSetCorrectUser() throws Exception {
        User testUser = UserFixture.aUser();
        mockedExternalContext = mock(ExternalContext.class);
        mockedHttpServletRequest = mock(HttpServletRequest.class);
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
    public void thatInitForPasswordIncorrectUserReturnErrorPage() throws Exception {
        mockedExternalContext = mock(ExternalContext.class);
        mockedHttpServletRequest = mock(HttpServletRequest.class);
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
    public void thatInitForPasswordReturnErrorPageIfNotLoggedIn() throws Exception {
        mockedExternalContext = mock(ExternalContext.class);
        mockedHttpServletRequest = mock(HttpServletRequest.class);
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
    public void thatValidateUniqueUsernameThrowsExceptionIfUsernameIsNotUniqueAndOtherUserId() throws Exception {
        User testUser = UserFixture.aUser();
        mockedValue = mock(Object.class);
        mockedUIComponent = mock(UIComponent.class);
        when(mockedValue.toString()).thenReturn("Michael");
        Map<String, Object> mapAttributes = new HashMap<>();
        mapAttributes.put("userId", 2L);
        when(mockedUIComponent.getAttributes()).thenReturn(mapAttributes);
        when(mockedUserService.findByUsername("Michael")).thenReturn(testUser);
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "edit.user.usernameNotUnique")).thenReturn(new FacesMessage("edit.user.usernameNotUnique"));

        try {
            userManagement.validateUniqueUsername(mockedFacesContext, mockedUIComponent, mockedValue);

            fail("should throw a ValidatorException");
        } catch (ValidatorException ex) {

        }
    }

    @Test
    public void thatValidateUniqueUsernameIsOkIfUsernameIsNotUniqueButSameUserId() throws Exception {
        User testUser = UserFixture.aUser();
        mockedValue = mock(Object.class);
        mockedUIComponent = mock(UIComponent.class);
        when(mockedValue.toString()).thenReturn("Michael");
        Map<String, Object> mapAttributes = new HashMap();
        mapAttributes.put("userId", 0L);
        when(mockedUIComponent.getAttributes()).thenReturn(mapAttributes);
        when(mockedUserService.findByUsername("Michael")).thenReturn(testUser);
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "edit.user.usernameNotUnique")).thenReturn(new FacesMessage("edit.user.usernameNotUnique"));

        try {
            userManagement.validateUniqueUsername(mockedFacesContext, mockedUIComponent, mockedValue);

        } catch (ValidatorException ex) {
            fail("shouldn´t throw a ValidatorException");
        }
    }

    @Test
    public void thatValidateUniqueUsernameIsOkIfNoUserWithSameNameWasFound() throws Exception {
        mockedValue = mock(Object.class);
        mockedUIComponent = mock(UIComponent.class);
        when(mockedValue.toString()).thenReturn("Michael");
        Map<String, Object> mapAttributes = new HashMap();
        mapAttributes.put("userId", 0L);
        when(mockedUIComponent.getAttributes()).thenReturn(mapAttributes);
        when(mockedUserService.findByUsername("Michael")).thenReturn(null);
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
    public void thatDoCancelReloadCorrectAtEditUser() {
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
    public void thatDoCancelReloadCorrectAtCreateUser() {
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

    /**
     * method under test: doSave
     */
    @Test
    public void thatDoSaveCreateOrUpdateUser() {
        userManagement.setUser(UserFixture.aUser());

        String result = userManagement.doSave();

        verify(mockedUserService).createOrUpdate(UserFixture.aUser());
        assertEquals("editUser.xhtml?faces-redirect=true", result);
    }

    /**
     *  method under test: doChangePassword
     */
    @Test
    public void thatDoChangePasswordIsCorrect() {
        userManagement.setUser(UserFixture.aUser());

        String result = userManagement.doChangePassword();

        assertNull(result);
        verify(mockedUserService).changePassword(UserFixture.aUser());
    }

    /**
     * method under test: doCancelChangePassword
     */
    @Test
    public void thatDoCancelChangePasswordInitForPasswordChange() {
        User testUser = UserFixture.aUser();
        mockedExternalContext = mock(ExternalContext.class);
        mockedHttpServletRequest = mock(HttpServletRequest.class);
        when(mockedFacesContext.getExternalContext()).thenReturn(mockedExternalContext);
        when(mockedExternalContext.getRequest()).thenReturn(mockedHttpServletRequest);
        Principal principalMichael = new PrincipalImpl("Michael");
        when(mockedHttpServletRequest.getUserPrincipal()).thenReturn(principalMichael);
        when(mockedUserService.findByUsername("Michael")).thenReturn(testUser);

        String result = userManagement.doCancelChangePassword();

        assertEquals("changePassword.xhtml", result);
    }

    /**
     * method under test: doDelete
     */
    @Test
    public void thatDoDeleteIsCorrect() {

        String result = userManagement.doDelete(UserFixture.aUser());

        verify(mockedUserService).delete(UserFixture.aUser());
        assertNull(result);
    }

    /**
     * method under test: allUsers
     */
    @Test
    public void thatAllUsersReturnListOfUsers() {
        when(mockedUserService.findAll()).thenReturn(Arrays.asList(UserFixture.aUser(),
                UserFixture.aUserWithRoleUser()));

        List<User> allUsers = userManagement.allUsers();

        assertEquals(2, allUsers.size());
        assertEquals(UserFixture.aUser(), allUsers.get(0));
        assertEquals(UserFixture.aUserWithRoleUser(), allUsers.get(1));
    }
}