package net.dumbcode.gradlehook;

import net.dumbcode.gradlehook.extensions.GradleHookExtension;
import net.dumbcode.gradlehook.tasks.UploadTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;

/**
 * @author Jack Goldsworth
 * @author Wyn Price
 */
public class GradleWebhookPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        GradleHookExtension extension = project.getExtensions().create("gradlehook", GradleHookExtension.class, project);

        TaskProvider<UploadTask> taskP = project.getTasks().register("post-request", UploadTask.class, uploadTask -> {
            uploadTask.getUrlToken().set(extension.getUrlToken());
            uploadTask.getFieldEntries().set(extension.getFieldEntries());
            uploadTask.getJars().set(extension.getAllJars());
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
