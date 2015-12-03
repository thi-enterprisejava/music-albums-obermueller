package de.thi.mymusic.util;

import javax.servlet.http.Part;
import java.io.File;


public class FileUtils {

    public static final String IMAGE_PATH = "C:\\Java Development Tools\\albumImages";

    public static String getFileNameFromPart(Part part) {
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

    public static String getFileTypFromPart(Part part) {
        String fileTyp = null;
        String fileName = getFileNameFromPart(part);
        if(fileName != null) {
            fileTyp = fileName.substring(fileName.lastIndexOf('.'));
        }

        return fileTyp;
    }

    /**
     * Delete a file
     *
     * @param file
     *        Complete path and filename
     * @return <code>true</code> if and only if the file was correctly deleted
     *         <code>false</code> otherwise
     */
    public static boolean deleteFile(String file) {
        boolean successful = false;
        if(file != null) {
            File image = new File(file);
            if(image.exists()) {
                successful = image.delete();
            }
        }

        return successful;
    }
}