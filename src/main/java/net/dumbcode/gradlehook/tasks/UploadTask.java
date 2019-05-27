package net.dumbcode.gradlehook.tasks;

import net.dumbcode.gradlehook.extensions.FieldEntry;
import net.dumbcode.gradlehook.extensions.JarEntry;
import net.dumbcode.gradlehook.tasks.form.FieldObject;
import net.dumbcode.gradlehook.tasks.form.FileObject;
import net.dumbcode.gradlehook.tasks.form.PostForm;
import org.gradle.api.DefaultTask;
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
    @Input
    private List<JarEntry> jars = new ArrayList<>();
    /**
     * The url to send a post request to
     */
    @Input
    private String urlToken;
    /**
     * The json payload to optionally send with the files. Sending this will cause a message/embed
     */
    @Input
    @Optional
    private List<FieldEntry> fieldEntries = new ArrayList<>();
    /**
     * If the payload is not empty, and this is set to true, then the json payload will be sent before the files
     */
    @Input
    @Optional
    private boolean messageFirst;

    public void setJars(List<JarEntry> jars) {
        this.jars = jars;
    }

    public void setUrlToken(String urlToken) {
        this.urlToken = urlToken;
    }

    public void setFieldEntries(List<FieldEntry> fieldEntries) {
        this.fieldEntries = fieldEntries;
    }

    public void setMessageFirst(boolean messageFirst) {
        this.messageFirst = messageFirst;
    }

    @TaskAction
    public void uploadFile() {
        String url = this.urlToken;
        //Create the form
        PostForm form = new PostForm(url);
        //If there is a json payload
        if(!this.fieldEntries.isEmpty()) {
            for (FieldEntry entry : this.fieldEntries) {
                String str = entry.getValue();

                //Replace the placeholders in the json file
                str = str.replace("{{version}}", getProject().getVersion().toString());
                str = str.replace("{{name}}", getProject().getName());
                str = str.replace("{{group}}", getProject().getGroup().toString());
                str = str.replace("{{datetime}}", Instant.now().atZone(ZoneOffset.UTC).toString());

                form.addObject(new FieldObject(entry.getName(), str));
            }

            //If the message first property is set, send the form and a reset it. The point of this is to have the text come before the files
            if(this.messageFirst) {
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
        for (JarEntry task : this.jars) {
            try {
                form.addObject(new FileObject(task.getJarFile(), task.getFileName()));
            } catch (IOException e) {
                System.out.printf("There was an error attaching the file %s %s", task.getFileName(), e.getCause().getLocalizedMessage());
            }
        }
        try {
            PostForm.Result result = form.send();
            int rCode = result.getResponseCode();
            if(rCode == HttpURLConnection.HTTP_OK || rCode == HttpURLConnection.HTTP_NO_CONTENT) {
                System.out.printf("File%s uploaded successfully", this.jars.size() == 1 ? "" : "s");
            } else {
                System.out.printf("File%s upload failed with response %d", this.jars.size() == 1 ? "" : "s", result.getResponseCode());
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());

        }

    }
}
