package net.dumbcode.gdisc.tasks;

import net.dumbcode.gdisc.extensions.JarEntry;
import net.dumbcode.gdisc.tasks.form.FieldObject;
import net.dumbcode.gdisc.tasks.form.FileObject;
import net.dumbcode.gdisc.tasks.form.PostForm;
import org.apache.tools.ant.types.resources.comparators.Date;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UploadTask extends DefaultTask {

    private final ListProperty<JarEntry> tasks = getProject().getObjects().listProperty(JarEntry.class);
    private final Property<String> urlToken = getProject().getObjects().property(String.class);
    private final Property<String> jsonPayload = getProject().getObjects().property(String.class);
    private final Property<Boolean> messageFirst = getProject().getObjects().property(Boolean.class);

    @Input
    public ListProperty<JarEntry> getTasks() {
        return tasks;
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

        PostForm form;
        try {
            form = new PostForm(url);
        } catch (MalformedURLException e) {
            System.out.printf("%s is an invalid URL", url);
            return;
        }
        if(this.jsonPayload.isPresent()) {
            String str = this.jsonPayload.get();
            str = str.replace("{{version}}", getProject().getVersion().toString());
            str = str.replace("{{name}}", getProject().getDisplayName());
            str = str.replace("{{group}}", getProject().getGroup().toString());
            str = str.replace("{{datetime}}", Instant.now().atZone(ZoneOffset.UTC).toString());

            form.addObject(new FieldObject("payload_json", str));
        }

        if(this.messageFirst.isPresent() && this.messageFirst.get()) {
            //Send the form and a create a new one. The point of this is to have the text come before the files
            try {
                PostForm.Result result = form.send();
                if(result.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    System.out.printf("Got an error response %d, aborting upload process", result.getResponseCode());
                }
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());

            }
            try {
                form = new PostForm(url);
            } catch (MalformedURLException e) {
                System.out.printf("%s is an invalid URL", url);
                return;
            }
        }

        List<JarEntry> tasks = this.tasks.getOrElse(new ArrayList<>());
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
