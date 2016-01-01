package de.thi.mymusic.validation;

import de.thi.mymusic.mocker.ContextMocker;
import de.thi.mymusic.util.GuiUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Michael on 17.12.2015.
 */

@RunWith(Theories.class)
public class ImageValidatorTest {
    /**
     * Class under test
     */
    ImageValidator imageValidator;

    FacesContext mockedFacesContext;
    UIComponent mockedUIComponent;
    Part mockedPartValue;
    GuiUtils mockedGuiUtils;

    @DataPoints
    public static String[] dataPoints = new String[] { "image/jpeg", "image/png", "image/gif"};

    @Before
    public void setUp() throws Exception {
        mockedUIComponent = mock(UIComponent.class);
        mockedFacesContext = ContextMocker.mockFacesContext();
        mockedGuiUtils = mock(GuiUtils.class);
        mockedPartValue = mock(Part.class);
        imageValidator = new ImageValidator(mockedGuiUtils);
    }

    /**
     * method under test: validate
     */

    @Theory
    public void thatValidateThrowNoErrorMessageIfFileIsCorrectImage(String imageFileType) throws Exception {
        when(mockedPartValue.getContentType()).thenReturn(imageFileType);
        when(mockedPartValue.getSize()).thenReturn(2024L * 1024L);

        try {
            imageValidator.validate(mockedFacesContext, mockedUIComponent, mockedPartValue);

        } catch (ValidatorException ex) {
            fail("Shouldn´t throw a ValidatorException");
        }

    }

    @Test (expected = ValidatorException.class)
    public void thatValidateThrowErrorMessageIfFileSizeToHigh() throws Exception {
        when(mockedPartValue.getContentType()).thenReturn("image/jpeg");
        when(mockedPartValue.getSize()).thenReturn(2024L * 1024L + 1L);
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "edit.album.validateError.imageFileToBig"))
                .thenReturn(new FacesMessage("edit.album.validateError.imageFileToBig"));

        imageValidator.validate(mockedFacesContext, mockedUIComponent, mockedPartValue);

    }

    @Test (expected = ValidatorException.class)
    public void thatValidateThrowErrorMessageIfFileIsNotAImage() throws Exception {
        when(mockedPartValue.getContentType()).thenReturn("application/pdf");
        when(mockedPartValue.getSize()).thenReturn(2024L * 1024L);
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "edit.album.validateError.noImageFile"))
                .thenReturn(new FacesMessage("edit.album.validateError.noImageFile"));

        imageValidator.validate(mockedFacesContext, mockedUIComponent, mockedPartValue);

    }
}