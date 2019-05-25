package net.dumbcode.gdisc.tasks;

import net.dumbcode.gdisc.DiscordPlugin;
import net.dumbcode.gdisc.tasks.form.FileObject;
import net.dumbcode.gdisc.tasks.form.PostForm;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class UploadTask extends DefaultTask {

    private final Property<String> urlToken = getProject().getObjects().property(String.class);
    private final Property<String> jsonPayload = getProject().getObjects().property(String.class);

    @Input
    public Property<String> getUrlToken() {
        return urlToken;
    }

    @TaskAction
    public void uploadFile() {
        if(!this.urlToken.isPresent()) {
            System.out.println("Url Token not present"); // todo : spefict what the user needs to do to link a token
            return;
        }
        String url = this.urlToken.get();
        PostForm postForm;
        try {
            postForm = new PostForm(url);
        } catch (MalformedURLException e) {
            System.out.printf("%s is not a valid URL", url);
            return;
        }

        try {
            postForm.addObject(new FileObject(null));//todo: add the file here
        } catch (IOException e) {
            e.printStackTrace(); //todo
        }

    }
}
