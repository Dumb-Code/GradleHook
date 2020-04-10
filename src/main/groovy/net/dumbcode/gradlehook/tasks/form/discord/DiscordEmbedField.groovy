package net.dumbcode.gradlehook.tasks.form.discord

import groovy.transform.builder.Builder

@Builder
class DiscordEmbedField {
    String name
    String value
    boolean inline
}
