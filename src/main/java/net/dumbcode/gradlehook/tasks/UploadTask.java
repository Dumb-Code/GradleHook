package net.dumbcode.gradlehook.tasks;

import net.dumbcode.gradlehook.extensions.JarEntry;
import net.dumbcode.gradlehook.tasks.form.FieldObject;
import net.dumbcode.gradlehook.tasks.form.FileObject;
import net.dumbcode.gradlehook.tasks.form.PostForm;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * The only task. Used to upload the files to a webhook
 */
public class UploadTask extends DefaultTask {

    /**
     * The list of all the jars to upload
     */
    private final ListProperty<JarEntry> jars = getProject().getObjects().listProperty(JarEntry.class);
    /**
     * The url to send a post request to
     */
    private final Property<String> urlToken = getProject().getObjects().property(String.class);
    /**
     * The json payload to optionally send with the files. Sending this will cause a message/embed
     */
    private final Property<String> jsonPayload = getProject().getObjects().property(String.class);
    /**
     * If the payload is not empty, and this is set to true, then the json payload will be sent before the files
     */
    private final Property<Boolean> messageFirst = getProject().getObjects().property(Boolean.class);

    @Input
    public ListProperty<JarEntry> getJars() {
        return jars;
    }

    @Input
    public Property<String> getUrlToken() {
        return urlToken;
    }

    @Input
    @Optional
    public Property<String> getJsonPayload() {
        return jsonPayload;
    }
    @Input
    @Optional
    public Property<Boolean> getMessageFirst() {
        return messageFirst;
    }

    @TaskAction
    public void uploadFile() {
        String url = this.urlToken.get();
        //Create the form
        PostForm form = new PostForm(url);
        //If there is a json payload
        if(this.jsonPayload.isPresent()) {
            String str = this.jsonPayload.get();

            //Replace the placeholders in the json file
            str = str.replace("{{version}}", getProject().getVersion().toString());
            str = str.replace("{{name}}", getProject().getDisplayName());
            str = str.replace("{{group}}", getProject().getGroup().toString());
            str = str.replace("{{datetime}}", Instant.now().atZone(ZoneOffset.UTC).toString());

            form.addObject(new FieldObject("payload_json", str));

            //If the message first property is set, send the form and a reset it. The point of this is to have the text come before the files
            if(this.messageFirst.isPresent() && this.messageFirst.get()) {
                try {
                    PostForm.Result result = form.send();
                    if(result.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        System.out.printf("Got an error response %d, aborting upload process", result.getResponseCode());
                    }
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());

                }
                form.reset();
            }
        }

        //Get the list of tasks
        List<JarEntry> tasks = this.jars.getOrElse(new ArrayList<>());
        for (JarEntry task : tasks) {
            try {
                form.addObject(new FileObject(task.getJarFile(), task.getFileName()));
            } catch (IOException e) {
                System.out.printf("There was an error attaching the file %s %s", task.getFileName(), e.getCause().getLocalizedMessage());
            }
        }
        try {
            PostForm.Result result = form.send();
            if(result.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.printf("File%s uploaded successfully", tasks.size() == 1 ? "" : "s");
            } else {
                System.out.printf("File%s upload failed with response %d", tasks.size() == 1 ? "" : "s", result.getResponseCode());
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());

        }

    }
}
