package de.thi.mymusic.util;

import de.thi.mymusic.mocker.PartMocker;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.servlet.http.Part;

import java.io.File;
import java.nio.file.Files;

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

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

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

    /**
     * method under test: deleteFile
     */
    @Test
    public void thatDeleteFileIsDeletingFile() throws Exception {
        File tempFile = testFolder.newFile("test.jpg");
        fileUtils.setImageDirectoryPath(testFolder.getRoot().getAbsolutePath());

        boolean successful = fileUtils.deleteFile("test.jpg");

        assertEquals(true, successful);
        assertEquals(false, tempFile.exists());
    }

    @Test
    public void thatDeleteFileNotDeletingFileIfNotExist() throws Exception {
        fileUtils.setImageDirectoryPath(testFolder.getRoot().getAbsolutePath());

        boolean successful = fileUtils.deleteFile("test.jpg");

        assertEquals(false, successful);
    }

    /**
     * method under test saveImageOnFilesystem
     */

    @Test
    public void thatSaveImageOnFilesystemSaveImage() throws Exception {
        fileUtils.setImageDirectoryPath(testFolder.getRoot().getAbsolutePath());
        File testFile = testFolder.newFile("test.jpg");
        Files.write(testFile.toPath(), "Testbild".getBytes());
        Part mockedImageFile = new PartMocker(testFile, null, "sdf;sdfd;filename=test.jpg;asdf;", 10000L);

        String filename = fileUtils.saveImageOnFilesystem(mockedImageFile);

        assertNotNull(filename);
        assertNotEquals("", filename);
        assertEquals(".jpg", filename.substring(filename.length()-4,filename.length()));
    }

    @Test
    public void thatSaveImageOnFilesystemSaveNoImageIfFileIsEmpty() throws Exception {
        fileUtils.setImageDirectoryPath(testFolder.getRoot().getAbsolutePath());
        File testFile = testFolder.newFile("test.jpg");
        Part mockedImageFile = new PartMocker(testFile, null, "sdf;sdfd;filename=test.jpg;asdf;", 0L);

        String filename = fileUtils.saveImageOnFilesystem(mockedImageFile);

        assertNull(filename);
    }


}