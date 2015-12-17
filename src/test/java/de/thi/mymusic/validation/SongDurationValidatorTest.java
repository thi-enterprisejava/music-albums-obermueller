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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Michael on 17.12.2015.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class, GuiUtils.class})
public class SongDurationValidatorTest {
    /**
     * Class under test
     */
    SongDurationValidator songDurationValidator;

    @Mock
    FacesContext mockedFacesContext;
    UIComponent mockedUIComponent;
    Object value;
    @Mock
    GuiUtils mockedGuiUtils;

    @Before
    public void setUp() throws Exception {
        songDurationValidator = new SongDurationValidator();
        mockedFacesContext = mock(FacesContext.class);
        mockedUIComponent = mock(UIComponent.class);
        PowerMockito.mockStatic(FacesContext.class);
        when(FacesContext.getCurrentInstance()).thenReturn(mockedFacesContext);
        PowerMockito.mockStatic(GuiUtils.class);
    }

    /**
     * method under test: validate
     */

    @Test
    public void ThatValidateThrowNoErrorMessageIfSongDurationIsCorrect() throws Exception {
        value = "03:12";

        try {
            songDurationValidator.validate(mockedFacesContext, mockedUIComponent, value);

        } catch (ValidatorException ex) {
            fail("Shouldn´t throw a ValidatorException");
        }

    }

    //TODO Datapoints with more incorrect values
    @Test (expected = ValidatorException.class)
    public void ThatValidateThrowExceptionIfDurationIsIncorrect() throws Exception {
        value = "05:60";
        when(mockedGuiUtils.getFacesMessage(mockedFacesContext, FacesMessage.SEVERITY_ERROR,
                "add.song.duration.formatError"))
                .thenReturn(new FacesMessage("add.song.duration.formatError"));

        songDurationValidator.validate(mockedFacesContext, mockedUIComponent, value);

    }




}