package de.thi.mymusic.validation;

import de.thi.mymusic.mocker.ContextMocker;
import de.thi.mymusic.util.GuiUtils;
import org.junit.Before;
import org.junit.Test;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Michael on 11.12.2015.
 */

public class PasswordValidatorTest {

    /**
     * Class under test
     */
    PasswordValidator passwordValidator;

    FacesContext mockedFacesContext;
    UIComponent mockedUIComponent;
    Object mockedValue;
    GuiUtils mockedGuiUtils;

    @Before
    public void setUp() throws Exception {
        mockedFacesContext = mock(FacesContext.class);
        mockedUIComponent = mock(UIComponent.class);
        mockedFacesContext = ContextMocker.mockFacesContext();
        mockedGuiUtils = mock(GuiUtils.class);
        mockedValue = mock(Object.class);
        passwordValidator = new PasswordValidator(mockedGuiUtils);
    }

    @Test
    public void thatEqualPasswordInputsAreCorrect(){
        when(mockedValue.toString()).thenReturn("password");
        UIInput uiInput= new UIInput();
        uiInput.setSubmittedValue("password");
        Map<String, Object> mapAttributes = new HashMap<>();
        mapAttributes.put("confirmPassword", uiInput);
        when(mockedUIComponent.getAttributes()).thenReturn(mapAttributes);
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "edit.user.passwordNotEqual")).thenReturn(new FacesMessage("edit.user.passwordNotEqual"));

        try {
            passwordValidator.validate(mockedFacesContext, mockedUIComponent, mockedValue);

        } catch(ValidatorException ex) {
            fail("Should not throw a ValidationException");
        }
    }

    @Test (expected = ValidatorException.class)
    public void thatNotEqualPasswordInputsThrowsValidationExceptionAndSetInputFieldFalse() {
        when(mockedValue.toString()).thenReturn("password");
        UIInput uiInput= new UIInput();
        uiInput.setSubmittedValue("password2");
        Map<String, Object> mapAttributes = new HashMap<>();
        mapAttributes.put("confirmPassword", uiInput);
        when(mockedUIComponent.getAttributes()).thenReturn(mapAttributes);

        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "edit.user.passwordNotEqual")).thenReturn(new FacesMessage("edit.user.passwordNotEqual"));

        passwordValidator.validate(mockedFacesContext, mockedUIComponent, mockedValue);

        assertThat("UIInput should be false", uiInput.isValid(), is(false));
    }
}