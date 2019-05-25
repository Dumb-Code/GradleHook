package net.dumbcode.gdisc.extensions;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public class TokenExtension
{
    private Property<String> token;

    public TokenExtension(Project project)
    {
        token = project.getObjects().property(String.class);
    }

    public Property<String> getToken()
    {
        return token;
    }
}