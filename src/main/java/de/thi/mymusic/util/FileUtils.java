package de.thi.mymusic.util;

import org.apache.log4j.Logger;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@Stateless
@PermitAll
public class FileUtils {

    //private String imageDirectoryPath = "C:\\Java Development Tools\\albumImages";
    private String imageDirectoryPath = "/tmp/images/mymusic";

    private static final Logger logger = Logger.getLogger(FileUtils.class);


    public void setImageDirectoryPath(String path) {
        this.imageDirectoryPath = path;
    }

    public String getFileNameFromPart(Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
                String fileName = content.substring(content.indexOf('=') + 1)
                        .trim().replace("\"", "");
                return fileName;
            }
        }
        return null;
    }

    public String getFileTypFromPart(Part part) {
        String fileTyp = null;
        String fileName = getFileNameFromPart(part);
        if(fileName != null) {
            fileTyp = fileName.substring(fileName.lastIndexOf('.'));
        }

        return fileTyp;
    }

    /**
     * Save image on filesystem
     *
     * @param imageFile
     * @return filename of saved image
     */
    public String saveImageOnFilesystem(Part imageFile) {
        String imageName = null;

        try {
            if (imageFile != null && imageFile.getSize() > 0) {
                String fileTyp = getFileTypFromPart(imageFile);
                Random random = new Random();
                long uuid = random.nextLong();
                imageName = uuid + fileTyp;
                Path filePath = Paths.get(imageDirectoryPath + File.separator  + imageName);
                Files.copy(imageFile.getInputStream(), filePath);

                logger.info("Image: " + imageName + " was uploaded and saved!");
            }
        } catch(IOException ex) {
            logger.error("Image couldn´t be loaded and saved!");
            return null;
        }

        return imageName;
    }

    /**
     * Delete a file
     *
     * @param filename
     *        filename
     * @return <code>true</code> if and only if the file was correctly deleted
     *         <code>false</code> otherwise
     */
    public boolean deleteFile(String filename) {
        Path pathToFile = Paths.get(imageDirectoryPath + File.separator + filename);
        try {
            return Files.deleteIfExists(pathToFile);
        } catch(IOException ex) {
            logger.error("Image could not be deleted: " + filename);
        }

        return false;
    }
}
