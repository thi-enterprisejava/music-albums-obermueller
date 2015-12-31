package de.thi.mymusic.util;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Part;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Michael on 31.12.2015.
 */
public class FileUtilsTest {

    /**
     * class under test
     */

    FileUtils fileUtils;

    Part mockedPart;

    @Before
    public void setUp() throws Exception {
        fileUtils = new FileUtils();
    }

    /**
     * method under test: getFileNameFromPart
     */

    @Test
    public void thatGetFileNameFromPartReturnCorrectFileName() {
        mockedPart = mock(Part.class);
        when(mockedPart.getHeader("content-disposition")).thenReturn("sdf;sdfd;filename=\"test.jpg\";asdf;");

        String filename = fileUtils.getFileNameFromPart(mockedPart);

        assertNotNull(filename);
        assertEquals("test.jpg", filename);
    }

    @Test
    public void thatGetFileNameFromPartReturnNullIfNoFilenameWasInHeader() throws Exception {
        mockedPart = mock(Part.class);
        when(mockedPart.getHeader("content-disposition")).thenReturn("sdf;sdfd;\"test.jpg\";asdf;");

        String filename = fileUtils.getFileNameFromPart(mockedPart);

        assertNull(filename);
    }

    /**
     * method under test: getFileTypFromPart
     */

    @Test
    public void thatGetFileTypFromPartReturnFileTypCorrect() throws Exception {
        mockedPart = mock(Part.class);
        when(mockedPart.getHeader("content-disposition")).thenReturn("sdf;sdfd;filename=\"test.jpg\";asdf;");

        String fileType = fileUtils.getFileTypFromPart(mockedPart);

        assertNotNull(fileType);
        assertEquals(".jpg", fileType);
    }

    @Test
    public void thatGetFileTypFromPartReturnNullIfNoFilenameWasFound() throws Exception {
        mockedPart = mock(Part.class);
        when(mockedPart.getHeader("content-disposition")).thenReturn("sdf;sdfd;asdf;");

        String fileType = fileUtils.getFileTypFromPart(mockedPart);

        assertNull(fileType);
    }

}