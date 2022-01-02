package net.dumbcode.gradlehook.tasks

import groovy.transform.ToString
import net.dumbcode.gradlehook.extensions.FieldEntry
import net.dumbcode.gradlehook.extensions.JarEntry
import net.dumbcode.gradlehook.tasks.form.FieldObject
import net.dumbcode.gradlehook.tasks.form.FileObject
import net.dumbcode.gradlehook.tasks.form.PostForm
import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import java.time.Instant
import java.time.ZoneOffset

/**
 * The only task. Used to upload the files to a webhook
 */
class UploadTask extends DefaultTask {
    /**
     * The list of all the jars to upload
     */
    @Input
    List<JarEntry> jars = new ArrayList<>()
    /**
     * The url to send a post request to
     */
    @Input
    @Optional
    String urlToken
    /**
     * The json payload to optionally send with the files. Sending this will cause a message/embed
     */
    @Input
    @Optional
    List<FieldEntry> fieldEntries = new ArrayList<>()
    /**
     * If the payload is not empty, and this is set to true, then the json payload will be sent before the files
     */
    @Input
    boolean messageFirst = false

    private final Logger logger = getProject().getLogger()

    @TaskAction
    void uploadFile() {
        def url = this.urlToken
        if(url === null) {
            throw new Error("URL WAS NULL.")
        }
        //Create the form
        def form = new PostForm()
        //If there is a json payload
        if(!this.fieldEntries.isEmpty()) {
            addJsonPayload(form, url)
        }
        //Get the list of tasks
        for (def task : this.jars) {
            try {
                form.addObject(new FileObject(task.getFileName(), task.getJarFile().getBytes()))
            } catch (IOException e) {
                logger.error("There was an error attaching the file {} {}", task.getFileName(), (e.cause?:e).localizedMessage)
            }
        }
        try {
            def result = form.send(url)
            int rCode = result.getResponseCode()
            if(rCode == HttpURLConnection.HTTP_OK || rCode == HttpURLConnection.HTTP_NO_CONTENT) {
                logger.quiet("File uploaded successfully")
            } else {
                logger.error("File upload failed with response {}", result.getResponseCode())
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private void addJsonPayload(PostForm form, String url)
    {
        for (def entry : this.fieldEntries) {
            String str = entry.getValue()

            //Replace the placeholders in the json file
            str = str.replace("{{version}}", getProject().getVersion().toString())
            str = str.replace("{{name}}", getProject().getName())
            str = str.replace("{{group}}", getProject().getGroup().toString())
            str = str.replace("{{datetime}}", Instant.now().atZone(ZoneOffset.UTC).toString())

            form.addObject(new FieldObject(entry.getName(), str))
        }

        //If the message first property is set, send the form and a reset it. The point of this is to have the text come before the files
        if(this.messageFirst) {
            try {
                PostForm.Result result = form.send(url)
                if(result.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    logger.error("Got an error response {}, aborting upload process", result.getResponseCode())
                }
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e)
            }
            form.reset()
        }
    }
}
