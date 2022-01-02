package net.dumbcode.gradlehook

import net.dumbcode.gradlehook.extensions.GradleHookExtension
import net.dumbcode.gradlehook.tasks.UploadTask
import net.dumbcode.gradlehook.tasks.form.discord.DiscordFormBuilder
import net.dumbcode.gradlehook.tasks.form.discord.DiscordFormBuilderProvider
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author Jack Goldsworth
 * @author Wyn Price
 */
class GradleWebhookPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def extension = project.extensions.create("gradlehook", GradleHookExtension)
        def task = project.tasks.create("postRequest", UploadTask)

        project.afterEvaluate { p ->
            task.jars = extension.jars
            task.urlToken = extension.urlToken
            task.fieldEntries = extension.fieldEntries
            task.messageFirst = extension.messageFirst

            task.dependsOn(project.tasks.getByName("build"))
        }


        task.description = "Posts the POST request specified by the `gradlehook` block"
        task.group = "upload"

        project.ext.set("DiscordBuilder", DiscordFormBuilderProvider.INSTANCE)
    }
}
