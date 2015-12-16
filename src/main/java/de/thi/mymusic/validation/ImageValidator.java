package de.thi.mymusic.validation;

import de.thi.mymusic.util.GuiUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

/**
 * Created by Michael Obermüller on 03.12.2015.
 */

@FacesValidator("imageValidator")
public class ImageValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        Part file = (Part) value;
        if(file != null) {
            if (!"image/jpeg".equals(file.getContentType())
                    && !"image/png".equals(file.getContentType())
                    && !"image/gif".equals(file.getContentType())) {
                FacesMessage msg = GuiUtils.getFacesMessage(context, FacesMessage.SEVERITY_ERROR,
                        "edit.album.validateError.noImageFile");

                throw new ValidatorException(msg);
            }

            if (file.getSize() > (2024 * 1024)) {
                FacesMessage msg = GuiUtils.getFacesMessage(context, FacesMessage.SEVERITY_ERROR,
                        "edit.album.validateError.imageFileToBig");

                throw new ValidatorException(msg);
            }
        }
    }

}
