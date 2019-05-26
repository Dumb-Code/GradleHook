package net.dumbcode.gdisc.tasks.form;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A field to be attached to the post request
 */
public class FieldObject implements FormObject {

    private final String fieldName;
    private final byte[] fieldValue;

    public FieldObject(String fieldName, String fieldValue) {
        this(fieldName, fieldValue.getBytes(StandardCharsets.UTF_8));
    }

    public FieldObject(String fieldName, File file) throws IOException {
        this(fieldName, FormObject.bytesFromFile(file));
    }

    public FieldObject(String fieldName, byte[] fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(("Content-Disposition: form-data; name=\"" + URLEncoder.encode(this.fieldName,"UTF-8") + "\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(this.fieldValue);
        baos.write("\r\n".getBytes(StandardCharsets.UTF_8));
        return baos.toByteArray();
    }
}