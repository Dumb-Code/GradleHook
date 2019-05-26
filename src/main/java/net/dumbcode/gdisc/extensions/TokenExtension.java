package net.dumbcode.gdisc.extensions;

import org.gradle.api.Project;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

public class TokenExtension {
    private Property<String> urlToken;
    private Property<String> jsonPayload;
    private Property<Boolean> messageFirst;

    private ListProperty<JarEntry> objs;

    public TokenExtension(Project project) {
        urlToken = project.getObjects().property(String.class);
        jsonPayload = project.getObjects().property(String.class);
        objs = project.getObjects().listProperty(JarEntry.class);
        messageFirst = project.getObjects().property(Boolean.class);
    }

    public void addFile(AbstractArchiveTask task) {
        objs.add(new JarEntry(task));
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

    public ListProperty<JarEntry> getAllTasks() {
        return objs;
    }
}