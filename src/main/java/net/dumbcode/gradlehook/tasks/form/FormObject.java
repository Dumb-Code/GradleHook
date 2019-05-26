package net.dumbcode.gradlehook.tasks.form;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * A form object is something to add to a post form. It holds all the information for the request
 */
public interface FormObject {
    /**
     * Get the bytes for the form.
     */
    byte[] getBytes() throws IOException;

    static byte[] bytesFromFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;

            while((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        }
    }
}
