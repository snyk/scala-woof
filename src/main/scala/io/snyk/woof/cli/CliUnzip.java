package io.snyk.woof.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;

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

            if (200 != responseCode || !contentTypeIsJson(response.getEntity())) {
                showError(response);
                return;
            }

            final String[] entries = new ObjectMapper().readValue(response.getEntity().getContent(), String[].class);

            System.out.println("The file contains these entries:");
            for (String entry : entries) {
                System.out.println(" * " + entry);
            }
        }
    }

    private void showError(HttpResponse response) throws IOException {
        System.err.println("An error occurred. Please check the server's logs for more information.");
        System.err.println();
        final HttpEntity entity = response.getEntity();
        if (contentTypeIsJson(entity)) {
            final JsonNode root = new ObjectMapper().readTree(entity.getContent());
            if (root.has("message")) {
                System.err.println("The server says: " + root.get("message").textValue());
            } else {
                System.err.println("The server said something: " + root);
            }
        } else {
            System.err.println("The server's response was not understood, response code: " +
                    response.getStatusLine().getStatusCode());
        }
    }

    private boolean contentTypeIsJson(HttpEntity entity) {
        for (HeaderElement element : entity.getContentType().getElements()) {
            if ("application/json".equals(element.getName())) {
                return true;
            }
        }

        return false;
    }
}
