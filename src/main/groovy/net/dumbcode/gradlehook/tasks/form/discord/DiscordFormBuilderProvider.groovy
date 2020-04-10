package net.dumbcode.gradlehook.tasks.form.discord

import groovy.json.JsonOutput
import net.dumbcode.gradlehook.tasks.form.FieldObject
import net.dumbcode.gradlehook.tasks.form.FileObject
import net.dumbcode.gradlehook.tasks.form.PostForm

import java.util.function.Function

enum DiscordFormBuilderProvider {
    INSTANCE

    PostForm createForm(@DelegatesTo(DiscordFormBuilder) Closure<?> closure) {
        def builder = new DiscordFormBuilder()
        builder.with(closure)
        println(JsonOutput.toJson(builder))
        def form = new PostForm()
                .addObject(new FieldObject("payload_json", JsonOutput.toJson(builder)))
        for(def file in builder.files) {
            form.addObject(new FileObject(file.name, file.data))
        }
        return form
    }

}