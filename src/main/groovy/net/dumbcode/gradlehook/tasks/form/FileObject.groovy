package net.dumbcode.gradlehook.tasks.form

import groovy.transform.TupleConstructor

import java.nio.charset.StandardCharsets

/**
 * A file to be attached to the post request
 */
@TupleConstructor
class FileObject implements FormObject {
    String filename
    byte[] bytes

    @Override
    byte[] getBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        baos.write(("Content-Disposition: form-data; name=\"" + this.filename + "\"; filename=\"" + this.filename + "\"\r\n\r\n").getBytes(StandardCharsets.UTF_8))
        baos.write(this.bytes)
        baos.write("\r\n".getBytes(StandardCharsets.UTF_8))
        return baos.toByteArray()
    }
}
