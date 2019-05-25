package net.dumbcode.gdisc.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public class UploadTask extends DefaultTask
{

    private final Property<String> token = getProject().getObjects().property(String.class);

    @Input
    public Property<String> getToken()
    {
        return token;
    }

    @TaskAction
    public void uploadFile()
    {
        System.out.println(token.get());
    }
}
