package net.dumbcode.gradlehook.tasks.form.discord


import groovy.transform.builder.Builder

@Builder
class DiscordEmbedAuthor {
    String name
    String url
    String icon_url
}
