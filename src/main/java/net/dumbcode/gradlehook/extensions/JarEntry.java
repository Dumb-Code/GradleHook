package net.dumbcode.gradlehook.extensions;

import org.gradle.api.tasks.bundling.AbstractArchiveTask;

import java.io.File;

/**
 * The individual jar to be uploaded. Holds the jarFile and the fileName of the jarFile.
 * To do, have this @{@link groovy.lang.DelegatesTo} to allow easy overrides
 */
public class JarEntry {
    private File jarFile;
    private String fileName;

    public JarEntry(AbstractArchiveTask task) {
        this.jarFile = task.getArchivePath();
        this.fileName = task.getArchiveName();
    }

    public File getJarFile() {
        return this.jarFile;
    }

    public String getFileName() {
        return this.fileName;
    }

    @Override
    public String toString() {
        return "JarEntry\n"
                + "filePath=" + this.jarFile.getAbsolutePath()
                + "fileName=" + this.fileName;
    }
}