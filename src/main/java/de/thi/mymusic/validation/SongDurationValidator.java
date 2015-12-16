package de.thi.mymusic.validation;

import de.thi.mymusic.util.GuiUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

/**
 * Created by Michael Obermüller on 12.11.2015.
 */

@FacesValidator("songDurationValidator")
public class SongDurationValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String duration = (String) value;

        if (!Pattern.matches("^([0-5]?\\d):([0-5]?\\d)$", duration)) {
            FacesMessage msg = GuiUtils.getFacesMessage(context, FacesMessage.SEVERITY_ERROR,
                    "add.song.duration.formatError");

            throw new ValidatorException(msg);
        }
    }

}
