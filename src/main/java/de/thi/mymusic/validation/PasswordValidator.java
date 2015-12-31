package de.thi.mymusic.validation;

import de.thi.mymusic.util.GuiUtils;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * PasswordValidator is responsible for validate password input fields
 */

@Named
@RequestScoped
public class PasswordValidator implements Validator {

    private GuiUtils guiUtils;

    @Inject
    public PasswordValidator(GuiUtils guiUtils) {
        this.guiUtils = guiUtils;
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {

        String password = value.toString();

        UIInput uiInputConfirmPassword = (UIInput) component.getAttributes()
                .get("confirmPassword");
        String confirmPassword = uiInputConfirmPassword.getSubmittedValue()
                .toString();

        // Password and confirmPassword are required
        if (password == null || password.isEmpty() || confirmPassword == null
                || confirmPassword.isEmpty()) {
            return;
        }

        // If password and confirmPassword aren´t equal
        if (!password.equals(confirmPassword)) {
            uiInputConfirmPassword.setValid(false);
            FacesMessage msg = guiUtils.getFacesMessage(context, FacesMessage.SEVERITY_ERROR,
                    "edit.user.passwordNotEqual");
            throw new ValidatorException(msg);
        }

    }
}

