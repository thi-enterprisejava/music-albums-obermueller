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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Michael on 17.12.2015.
 */

@RunWith(Theories.class)
public class SongDurationValidatorTest {
    /**
     * Class under test
     */
    SongDurationValidator songDurationValidator;

    FacesContext mockedFacesContext;
    UIComponent mockedUIComponent;
    Object value;
    GuiUtils mockedGuiUtils;

    @DataPoints
    public static String[] incorrectDurations = new String[] { "05:60", "adsf 1123 ", " 2359:59", "01:80",
        "09:1z"};


    @Before
    public void setUp() throws Exception {
        mockedUIComponent = mock(UIComponent.class);
        mockedFacesContext = ContextMocker.mockFacesContext();
        mockedGuiUtils = mock(GuiUtils.class);
        songDurationValidator = new SongDurationValidator(mockedGuiUtils);
    }

    /**
     * method under test: validate
     */

    @Test
    public void thatValidateThrowNoErrorMessageIfSongDurationIsCorrect() throws Exception {
        value = "03:12";

        try {
            songDurationValidator.validate(mockedFacesContext, mockedUIComponent, value);

        } catch (ValidatorException ex) {
            fail("Shouldn´t throw a ValidatorException");
        }

    }

    @Theory
    public void thatValidateThrowExceptionIfDurationIsIncorrect(String value) throws Exception {
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "add.song.duration.formatError"))
                .thenReturn(new FacesMessage("add.song.duration.formatError"));
        try {
            songDurationValidator.validate(mockedFacesContext, mockedUIComponent, value);
            fail("Should throw a ValidatorException");
        }
        catch (ValidatorException ex) {

        }
    }
}