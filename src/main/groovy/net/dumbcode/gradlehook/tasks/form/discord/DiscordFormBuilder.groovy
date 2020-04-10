package net.dumbcode.gradlehook.tasks.form.discord


import groovy.transform.builder.Builder
import org.gradle.api.tasks.bundling.AbstractArchiveTask

@Builder
class DiscordFormBuilder {
    String username
    String content
    String avatar_url
    boolean tts
    transient List<DiscordFile> files = new ArrayList<>()
    def embeds = new ArrayList<DiscordEmbed>()

    void addFile(File file) {
        this.addFile(file.name, file.bytes)
    }

    void addFile(String fileName, byte[] data) {
        this.files.add(new DiscordFile(fileName, data))
    }

    void addFileJarTask(AbstractArchiveTask task) {
        this.files.add(new DiscordFile(task.archiveName, task.archivePath.bytes))
    }

    void embed(@DelegatesTo(DiscordEmbed) Closure<?> closure) {
        def embed = new DiscordEmbed()
        embed.with(closure)
        this.embeds.add(embed)
    }
}
