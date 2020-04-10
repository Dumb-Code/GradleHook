package net.dumbcode.gradlehook.tasks.form.discord

import groovy.transform.TupleConstructor

@TupleConstructor
class DiscordFile {
    String name
    byte[] data
}
