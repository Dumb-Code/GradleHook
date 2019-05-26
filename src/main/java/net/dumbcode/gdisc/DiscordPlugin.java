package net.dumbcode.gdisc;

import net.dumbcode.gdisc.extensions.TokenExtension;
import net.dumbcode.gdisc.tasks.UploadTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

public class DiscordPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        TokenExtension extension = project.getExtensions().create("discordplugin", TokenExtension.class, project);

        TaskProvider<UploadTask> taskP = project.getTasks().register("upload-file", UploadTask.class, uploadTask -> {
            uploadTask.getUrlToken().set(extension.getUrlToken());
            uploadTask.getJsonPayload().set(extension.getJsonPayload());
            uploadTask.getTasks().set(extension.getAllTasks());
            uploadTask.getMessageFirst().set(extension.getMessageFirst());
        });

        if (taskP.isPresent()) {
            UploadTask task = taskP.get();
            task.dependsOn(project.getTasks().getByName("build"));
            task.setDescription("Desc");
            task.setGroup("upload");
        }
    }
}
