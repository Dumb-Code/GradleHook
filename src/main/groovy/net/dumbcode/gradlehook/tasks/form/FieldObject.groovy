package net.dumbcode.gradlehook.tasks.form


import java.nio.charset.StandardCharsets

/**
 * A field to be attached to the post request
 */
class FieldObject implements FormObject {

    private final String fieldName
    private final byte[] fieldValue

    FieldObject(String fieldName, String fieldValue) {
        this(fieldName, fieldValue.getBytes(StandardCharsets.UTF_8))
    }

    FieldObject(String fieldName, byte[] fieldValue) {
        this.fieldName = fieldName
        this.fieldValue = fieldValue
    }

    @Override
    byte[] getBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        baos.write(("Content-Disposition: form-data; name=\"" + URLEncoder.encode(this.fieldName,"UTF-8") + "\"\r\n\r\n").getBytes(StandardCharsets.UTF_8))
        baos.write(this.fieldValue)
        baos.write("\r\n".getBytes(StandardCharsets.UTF_8))
        return baos.toByteArray()
    }
}