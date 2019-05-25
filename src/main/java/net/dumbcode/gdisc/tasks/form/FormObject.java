package net.dumbcode.gdisc.tasks.form;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public interface FormObject {
    byte[] getBytes() throws IOException;

    static byte[] getBytes(File file) throws IOException {
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
