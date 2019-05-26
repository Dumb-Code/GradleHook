package net.dumbcode.gradlehook.extensions;

import org.gradle.api.Project;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

/**
 * The extension class for the plugin/
 */
public class TokenExtension {
    /**
     * The list of all the jars to upload
     */
    private ListProperty<JarEntry> jars;
    /**
     * The url to send a post request to
     */
    private Property<String> urlToken;
    /**
     * The json payload to optionally send with the files. Sending this will cause a message/embed
     */
    private Property<String> jsonPayload;
    /**
     * If the payload is not empty, and this is set to true, then the json payload will be sent before the files
     */
    private Property<Boolean> messageFirst;

    public TokenExtension(Project project) {
        urlToken = project.getObjects().property(String.class);
        jsonPayload = project.getObjects().property(String.class);
        jars = project.getObjects().listProperty(JarEntry.class);
        messageFirst = project.getObjects().property(Boolean.class);
    }

    public void addArtifact(AbstractArchiveTask task) {
        jars.add(new JarEntry(task));
    }

    public void urlToken(String string) {
        this.urlToken.set(string);
    }

    public void jsonPayload(String string) {
        this.jsonPayload.set(string);
    }

    public void messageFirst() {
        this.messageFirst.set(true);
    }

    public Property<String> getUrlToken() {
        return urlToken;
    }

    public Property<String> getJsonPayload() {
        return jsonPayload;
    }

    public Property<Boolean> getMessageFirst() {
        return messageFirst;
    }

    public ListProperty<JarEntry> getAllJars() {
        return jars;
    }
}