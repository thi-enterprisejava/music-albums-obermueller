package de.thi.mymusic.mocker;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;


public class PartMocker implements Part {

    private File file;
    private String contentType;
    private HashMap<String, String> headers = new HashMap<>();
    private long partSize;

    public PartMocker(File file, String contentType, String contentDisposition, long size) {
        this.file = file;
        this.contentType = contentType;
        headers.put("content-disposition", contentDisposition);
        this.partSize = size;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getSubmittedFileName() {
        return null;
    }

    @Override
    public long getSize() {
        return partSize;
    }

    @Override
    public void write(String fileName) throws IOException {

    }

    @Override
    public void delete() throws IOException {
        Files.deleteIfExists(file.toPath());
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }
}
