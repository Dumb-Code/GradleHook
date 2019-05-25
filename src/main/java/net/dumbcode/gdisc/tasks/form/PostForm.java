package net.dumbcode.gdisc.tasks.form;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostForm {

    private final String url;

    private final List<FormObject> objects = new ArrayList<>();

    private final String boundary = UUID.randomUUID().toString();
    private final byte[] boundaryBytes = ("--" + this.boundary + "\r\n").getBytes();
    private final byte[] finishBoundaryBytes = ("--" + this.boundary + "--").getBytes(StandardCharsets.UTF_8);

    public PostForm(String url) throws MalformedURLException {
        this.url = url;
    }

    public PostForm addObject(FormObject object) {
        this.objects.add(object);
        return this;
    }

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
