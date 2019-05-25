package net.dumbcode.gdisc.extensions;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public class TokenExtension {
    private Property<String> urlToken;
    private Property<String> jsonPayload;

    public TokenExtension(Project project) {
        urlToken = project.getObjects().property(String.class);
        jsonPayload = project.getObjects().property(String.class);
    }

    public Property<String> getUrlToken() {
        return urlToken;
    }

    public Property<String> getJsonPayload() {
        return jsonPayload;
    }
}