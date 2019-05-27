package net.dumbcode.gradlehook;

import net.dumbcode.gradlehook.extensions.GradleHookExtension;
import net.dumbcode.gradlehook.tasks.UploadTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @author Jack Goldsworth
 * @author Wyn Price
 */
public class GradleWebhookPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        GradleHookExtension extension = project.getExtensions().create("gradlehook", GradleHookExtension.class, project);

        UploadTask task = project.getTasks().create("postRequest", UploadTask.class);

        project.afterEvaluate(p -> {
            task.setJars(extension.getAllJars());
            task.setUrlToken(extension.getUrlToken());
            task.setFieldEntries(extension.getFieldEntries());
            task.setMessageFirst(extension.getMessageFirst());
        });

        task.dependsOn(project.getTasks().getByName("build"));
        task.setDescription("Posts the POST request specified by the `gradlehook` block");
        task.setGroup("upload");
    }
}
