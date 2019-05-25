package net.dumbcode.gdisc;

import net.dumbcode.gdisc.extensions.TokenExtension;
import net.dumbcode.gdisc.tasks.UploadTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DiscordPlugin implements Plugin<Project>
{

    @Override
    public void apply(Project project)
    {
        TokenExtension extension = project.getExtensions().create("discordplugin", TokenExtension.class, project);
        project.getTasks().register("upload-file", UploadTask.class, uploadTask -> uploadTask.getToken().set(extension.getToken()));
    }
}
