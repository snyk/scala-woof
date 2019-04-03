package io.snyk.woof.server

import io.snyk.woof.app.ZipHandler
import org.glassfish.jersey.media.multipart.FormDataParam
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import java.io.InputStream
import java.util

@Path("unzip")
@Produces(Array(MediaType.APPLICATION_JSON)) class UnzipResource(val handler: ZipHandler) {
  @POST
  @Consumes(Array(MediaType.MULTIPART_FORM_DATA))
  @throws[Exception]
  def unzip(@FormDataParam("file") data: InputStream): Array[String] = handler.listTopLevelEntries(data)
}
