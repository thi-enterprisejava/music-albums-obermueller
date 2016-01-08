package de.thi.mymusic.validation;

import de.thi.mymusic.util.GuiUtils;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 * ImageValidator is responsible for validate uploaded image files
 *
 */
@Named
@RequestScoped
public class ImageValidator implements Validator {
    private static long MAX_FILE_SIZE = 2_024L * 1_024L;
    private GuiUtils guiUtils;

    @Inject
    public ImageValidator(GuiUtils guiUtils) {
        this.guiUtils = guiUtils;
    }

    /**
     * Validate if uploaded image file is below max file size and is file typ jpeg, png or gif
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        Part file = (Part) value;
        if(file != null) {
            if (!"image/jpeg".equals(file.getContentType())
                    && !"image/png".equals(file.getContentType())
                    && !"image/gif".equals(file.getContentType())) {
                FacesMessage msg = guiUtils.getFacesMessage(context, FacesMessage.SEVERITY_ERROR,
                        "edit.album.validateError.noImageFile");

                throw new ValidatorException(msg);
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                FacesMessage msg = guiUtils.getFacesMessage(context, FacesMessage.SEVERITY_ERROR,
                        "edit.album.validateError.imageFileToBig");

                throw new ValidatorException(msg);
            }
        }
    }

}
