package net.dumbcode.gradlehook.tasks.form.discord

import groovy.transform.builder.Builder

import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

@Builder
class DiscordEmbed {
    int color
    DiscordEmbedAuthor author
    String title
    String url
    String description
    def fields = new ArrayList<DiscordEmbedField>()
    DiscordEmbedImage image
    DiscordEmbedImage thumbnail
    DiscordEmbedFooter footer
    String timestamp

    void author(@DelegatesTo(DiscordEmbedAuthor) Closure<?> closure) {
        this.author = new DiscordEmbedAuthor()
        this.author.with(closure)
    }

    void field(@DelegatesTo(DiscordEmbedField) Closure<?> closure) {
        def field = new DiscordEmbedField()
        field.with(closure)
        this.fields.add(field)
    }

    void image(@DelegatesTo(DiscordEmbedImage) Closure<?> closure) {
        this.image = new DiscordEmbedImage()
        this.image.with(closure)
    }

    void thumbnail(@DelegatesTo(DiscordEmbedImage) Closure<?> closure) {
        this.thumbnail = new DiscordEmbedImage()
        this.thumbnail.with(closure)
    }

    void footer(@DelegatesTo(DiscordEmbedFooter) Closure<?> closure) {
        this.footer = new DiscordEmbedFooter()
        this.footer.with(closure)
    }

    void timestampNow() {
        this.timestamp(Instant.now())
    }

    void timestamp(TemporalAccessor time) {
        this.timestamp = DateTimeFormatter.ISO_INSTANT.format(time)
    }

}
