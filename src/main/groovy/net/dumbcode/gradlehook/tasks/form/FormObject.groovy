package net.dumbcode.gradlehook.tasks.form
/**
 * A form object is something to add to a post form. It holds all the information for the request
 */
interface FormObject {
    /**
     * @return the serialized version for the http request
     * @throws IOException if serializing has gone wrong
     */
    byte[] getBytes() throws IOException;
}
