package net.dumbcode.gdisc.tasks.form;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileObject implements FormObject {

    private final String filename;
    private final byte[] bytes;

    public FileObject(File file) throws IOException {
        this(file.getName(), FormObject.getBytes(file));
    }

    //filename has the file extension on it
    public FileObject(String filename, byte[] bytes) {
        this.filename = filename;
        this.bytes = bytes;
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //todo: have name=this.filename output a non extension filename
        baos.write(("Content-Disposition: form-data; name=\"" + this.filename + "\"; filename=\"" + this.filename + "\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(this.bytes);
        baos.write("\r\n".getBytes(StandardCharsets.UTF_8));
        return baos.toByteArray();
    }
}
