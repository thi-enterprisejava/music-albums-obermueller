package de.thi.mymusic.validation;

import de.thi.mymusic.util.GuiUtils;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.regex.Pattern;

/**
 * SongDurationValidator is responsible for validate input of song duration
 */

@Named
@RequestScoped
public class SongDurationValidator implements Validator {

    private GuiUtils guiUtils;

    @Inject
    public SongDurationValidator(GuiUtils guiUtils) {
        this.guiUtils = guiUtils;
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String duration = (String) value;

        if (!Pattern.matches("^([0-5]?\\d):([0-5]?\\d)$", duration)) {
            FacesMessage msg = guiUtils.getFacesMessage(context, FacesMessage.SEVERITY_ERROR,
                    "add.song.duration.formatError");

            throw new ValidatorException(msg);
        }
    }

}
