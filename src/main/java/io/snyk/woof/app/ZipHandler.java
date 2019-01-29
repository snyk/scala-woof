package io.snyk.woof.app;

import com.google.common.io.ByteStreams;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import net.lingala.zip4j.core.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ZipHandler {
    public List<String> listTopLevelEntries(InputStream zipStream) throws Exception {
        final File temp = Files.createTempFile("to-extract", ".zip").toFile();
        try {
            // save our uploaded file in a temporary file
            try (OutputStream os = new FileOutputStream(temp)) {
                ByteStreams.copy(zipStream, os);
            }

            // open it as a zip file
            final ZipFile zip = new ZipFile(temp);

            final Path tempDir = Files.createTempDirectory("woof");
            try {
                // extract the contents to a temporary directory
                zip.extractAll(tempDir.toAbsolutePath().toString());

                // list the directory, and find the names of the items in it
                return Files.list(tempDir)
                        .map(p -> p.getFileName().toString())
                        .collect(Collectors.toList());
            } finally {
                // The temporary directory only contains zip entries.
                // Zip files do not support symlinks.
                // Hence, this is safe. (For real!)
                // Java 8 on OSX does not support secure deletion, so we have to
                // disable security.
                MoreFiles.deleteRecursively(tempDir, RecursiveDeleteOption.ALLOW_INSECURE);
            }
        } finally {
            if (!temp.delete()) {
                System.err.println("temporary file cleanup failed: " + temp);
            }
        }
    }
}
