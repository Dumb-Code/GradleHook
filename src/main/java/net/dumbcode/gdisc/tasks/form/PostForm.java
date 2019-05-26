package net.dumbcode.gdisc.tasks.form;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Used to send a POST request to the webhook. Holds the objects to be sent over.
 */
public class PostForm {

    /**
     * The url to send to
     */
    private final String url;

    /**
     * A list of objects to send to
     */
    private final List<FormObject> objects = new ArrayList<>();

    /**
     * The boundaries for the data
     */
    private final String boundary = UUID.randomUUID().toString();
    private final byte[] boundaryBytes = ("--" + this.boundary + "\r\n").getBytes();
    private final byte[] finishBoundaryBytes = ("--" + this.boundary + "--").getBytes(StandardCharsets.UTF_8);

    public PostForm(String url) {
        this.url = url;
    }

    /**
     * Adds an object to the list of objects.
     * @param object the object to be added to the request
     * @return itself
     */
    public PostForm addObject(FormObject object) {
        this.objects.add(object);
        return this;
    }

    /**
     * Sends the webhook to the url.
     * @return The result of that webhook.
     * @throws IOException If something goes wrong
     */
    public Result send() throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.boundary);
        http.setChunkedStreamingMode(0);
        http.setRequestProperty("User-Agent", "Mozilla/5.0");

        try(OutputStream out = http.getOutputStream()) {
            for (FormObject object : this.objects) {
                out.write(this.boundaryBytes);
                out.write(object.getBytes());
            }
            out.write(this.finishBoundaryBytes);

            out.flush();
        }
        return new Result(http);
    }

    /**
     * Resets the webhook. Removes all the form objects, but keeps the {@link #boundary}
     */
    public void reset() {
        this.objects.clear();
    }

    /**
     * The result of a webhook. Holds the information about the response code and the result.
     */
    public class Result {
        private final int responseCode;
        private final String result;

        public Result(HttpURLConnection con) throws IOException {
            this.responseCode = con.getResponseCode();

            if (this.responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                this.result = response.toString();
            } else {
                this.result = null;
            }
        }

        public int getResponseCode() {
            return responseCode;
        }

        public String getResult() {
            return result;
        }
    }
}
