package io.snyk.woof.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import io.snyk.woof.app.ZipHandler;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileInputStream;

public class CliUnzip extends Command {
    public CliUnzip() {
        super("list-zip", "lists a zip file");
    }

    @Override
    public void configure(Subparser subparser) {
        subparser.addArgument("file")
                .type(File.class)
                .required(false)
                .nargs("?")
                .setDefault("zip-slip.zip");
    }

    @Override
    public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
        final String path = namespace.getString("file");
        if (null == path) {
            throw new IllegalArgumentException("no file, despite default");
        }

        final File file = new File(path);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            final MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addPart("file", new FileBody(file, ContentType.DEFAULT_BINARY));

            final HttpPost post = new HttpPost("http://localhost:8080/api/unzip");
            post.setEntity(builder.build());
            HttpResponse response = client.execute(post);
            final int responseCode = response.getStatusLine().getStatusCode();
            if (200 != responseCode) {
                throw new IllegalStateException("invalid response, code: " + responseCode);
            }

            final String[] entries = new ObjectMapper().readValue(response.getEntity().getContent(), String[].class);

            System.out.println("The file contains these entries:");
            for (String entry : entries) {
                System.out.println(" * " + entry);
            }
        }
    }
}
