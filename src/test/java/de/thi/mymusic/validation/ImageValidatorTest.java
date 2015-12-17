package de.thi.mymusic.validation;

import de.thi.mymusic.util.GuiUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class, GuiUtils.class})
public class ImageValidatorTest {
    /**
     * Class under test
     */
    ImageValidator imageValidator;

    @Mock
    FacesContext mockedFacesContext;
    UIComponent mockedUIComponent;
    Part mockedPartValue;
    @Mock
    GuiUtils mockedGuiUtils;

    //@DataPoints
    //public static String[] dataPoints = new String[] { "image/jpeg", "image/png", "image/gif"};

    @Before
    public void setUp() throws Exception {
        imageValidator = new ImageValidator();
        mockedFacesContext = mock(FacesContext.class);
        mockedUIComponent = mock(UIComponent.class);
        PowerMockito.mockStatic(FacesContext.class);
        when(FacesContext.getCurrentInstance()).thenReturn(mockedFacesContext);
        PowerMockito.mockStatic(GuiUtils.class);
        mockedPartValue = mock(Part.class);
    }

    /**
     * method under test: validate
     */

    //TODO Datapoints for other ImageFileTypes
    @Test
    public void ThatValidateThrowNoErrorMessageIfFileIsCorrectImage() throws Exception {
        when(mockedPartValue.getContentType()).thenReturn("image/jpeg");
        when(mockedPartValue.getSize()).thenReturn(2024L * 1024L);

        try {
            imageValidator.validate(mockedFacesContext, mockedUIComponent, mockedPartValue);

        } catch (ValidatorException ex) {
            fail("Shouldn´t throw a ValidatorException");
        }

    }

    @Test (expected = ValidatorException.class)
    public void ThatValidateThrowErrorMessageIfFileSizeToHigh() throws Exception {
        when(mockedPartValue.getContentType()).thenReturn("image/jpeg");
        when(mockedPartValue.getSize()).thenReturn(2024L * 1024L + 1L);
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "edit.album.validateError.imageFileToBig"))
                .thenReturn(new FacesMessage("edit.album.validateError.imageFileToBig"));

        imageValidator.validate(mockedFacesContext, mockedUIComponent, mockedPartValue);

    }

    @Test (expected = ValidatorException.class)
    public void ThatValidateThrowErrorMessageIfFileIsNotAImage() throws Exception {
        when(mockedPartValue.getContentType()).thenReturn("application/pdf");
        when(mockedPartValue.getSize()).thenReturn(2024L * 1024L);
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "edit.album.validateError.noImageFile"))
                .thenReturn(new FacesMessage("edit.album.validateError.noImageFile"));

        imageValidator.validate(mockedFacesContext, mockedUIComponent, mockedPartValue);

    }
}