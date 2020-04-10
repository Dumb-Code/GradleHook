package net.dumbcode.gradlehook.tasks.form.discord

import groovy.transform.builder.Builder

@Builder
class DiscordEmbedFooter {
    String text
    String icon_url
}
