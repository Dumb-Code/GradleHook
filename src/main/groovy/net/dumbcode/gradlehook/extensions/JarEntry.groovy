package net.dumbcode.gradlehook.extensions

import groovy.transform.ToString
import groovy.transform.TupleConstructor
import org.gradle.api.tasks.bundling.AbstractArchiveTask

/**
 * The individual jar to be uploaded. Holds the jarFile and the fileName of the jarFile.
 * To do, have this @{@link groovy.lang.DelegatesTo} to allow easy overrides
 */
@ToString
@TupleConstructor
class JarEntry {
    File jarFile
    String fileName

    JarEntry(AbstractArchiveTask task) {
        this.jarFile = task.getArchivePath()
        this.fileName = task.getArchiveName()
    }

}