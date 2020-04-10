package net.dumbcode.gradlehook.tasks.form

import groovy.transform.ToString

import java.nio.charset.StandardCharsets

/**
 * Used to send a POST request to the webhook. Holds the objects to be sent over.
 */
class PostForm {

    /**
     * A list of objects to send to
     */
    final List<FormObject> objects = new ArrayList<>()

    /**
     * The boundaries for the data
     */
    private final String boundary = UUID.randomUUID().toString()
    private final byte[] boundaryBytes = ("--${this.boundary}\r\n").getBytes()
    private final byte[] finishBoundaryBytes = ("--${this.boundary}--").getBytes(StandardCharsets.UTF_8)

    /**
     * Adds an object to the list of objects.
     * @param object the object to be added to the request
     * @return itself
     */
    PostForm addObject(FormObject object) {
        this.objects.add(object)
        return this
    }

    /**
     * Sends the webhook to the url.
     * @return The result of that webhook.
     * @throws IOException If something goes wrong
     */
    Result send(String url) throws IOException {
        HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection()
        http.requestMethod = "POST"
        http.doOutput = true
        http.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.boundary)
        http.chunkedStreamingMode = 0
        http.setRequestProperty("User-Agent", "Mozilla/5.0")


        http.getOutputStream().withCloseable {
            for (FormObject object : this.objects) {
                it.write(this.boundaryBytes)
                it.write(object.getBytes())
            }
            it.write(this.finishBoundaryBytes)
            it.flush()
        }
        return new Result(http)
    }

    /**
     * Resets the webhook. Removes all the form objects, but keeps the {@link #boundary}
     */
    void reset() {
        this.objects.clear()
    }

    /**
     * The result of a webhook. Holds the information about the response code and the result.
     */
    @ToString
    class Result {
        final int responseCode
        final String responseMessage

        Result(HttpURLConnection con) throws IOException {
            this.responseCode = con.responseCode
            this.responseMessage = con.inputStream.text
        }
    }
}
