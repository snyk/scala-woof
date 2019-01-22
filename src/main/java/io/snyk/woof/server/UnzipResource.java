package io.snyk.woof.server;

import io.snyk.woof.app.ZipHandler;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.List;

@Path("unzip")
@Produces(MediaType.APPLICATION_JSON)
public class UnzipResource {
    private final ZipHandler handler;

    public UnzipResource(ZipHandler handler) {
        this.handler = handler;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public List<String> unzip(@FormDataParam("file") InputStream data) throws Exception {
        return handler.listTopLevelEntries(data);
    }
}
