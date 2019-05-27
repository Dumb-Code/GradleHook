package net.dumbcode.gradlehook.extensions;

import org.gradle.api.Project;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

import java.util.ArrayList;
import java.util.List;

/**
 * The extension class for the plugin/
 */
public class GradleHookExtension {
    /**
     * The list of all the jars to upload
     */
    private List<JarEntry> jars = new ArrayList<>();
    /**
     * The url to send a post request to
     */
    private String urlToken;
    /**
     * The json payload to optionally send with the files. Sending this will cause a message/embed
     */
    private List<FieldEntry> fieldEntries = new ArrayList<>();
    /**
     * If the payload is not empty, and this is set to true, then the json payload will be sent before the files
     */
    private boolean messageFirst;

    public GradleHookExtension(Project project) {
    }

    public void addArtifact(AbstractArchiveTask task) {
        jars.add(new JarEntry(task));
    }

    public void urlToken(String string) {
        this.urlToken = string;
    }

    public void addField(String name, String value) {
        this.fieldEntries.add(new FieldEntry(name, value));
    }

    public void messageFirst() {
        this.messageFirst = true;
    }

    public String getUrlToken() {
        return urlToken;
    }

    public List<FieldEntry> getFieldEntries() {
        return fieldEntries;
    }

    public boolean getMessageFirst() {
        return messageFirst;
    }

    public List<JarEntry> getAllJars() {
        return jars;
    }
}