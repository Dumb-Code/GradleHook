package net.dumbcode.gradlehook.tasks.form;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * A file to be attached to the post request
 */
public class FileObject implements FormObject {

    private final String filename;
    private final byte[] bytes;

    public FileObject(File file, String name) throws IOException {
        this(name, FormObject.bytesFromFile(file));
    }

    public FileObject(String filename, byte[] bytes) {
        this.filename = filename;
        this.bytes = bytes;
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(("Content-Disposition: form-data; name=\"" + this.filename + "\"; filename=\"" + this.filename + "\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(this.bytes);
        baos.write("\r\n".getBytes(StandardCharsets.UTF_8));
        return baos.toByteArray();
    }
}
