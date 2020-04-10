package net.dumbcode.gradlehook.extensions

import org.gradle.api.Project
import org.gradle.api.tasks.bundling.AbstractArchiveTask

import java.util.ArrayList
import java.util.List

/**
 * The extension class for the plugin/
 */
class GradleHookExtension {
    /**
     * The list of all the jars to upload
     */
    def jars = new ArrayList<JarEntry>()
    /**
     * The url to send a post request to
     */
    def urlToken = ""
    /**
     * The json payload to optionally send with the files. Sending this will cause a message/embed
     */
    def fieldEntries = new ArrayList<FieldEntry>()
    /**
     * If the payload is not empty, and this is set to true, then the json payload will be sent before the files
     */
    def messageFirst = false

    GradleHookExtension() {
    }

    void addArtifact(AbstractArchiveTask task) {
        jars.add(new JarEntry(task))
    }

    void addField(String name, String value) {
        this.fieldEntries.add(new FieldEntry(name, value))
    }
}