package io.snyk.woof.cli

import java.io.{File, IOException}

import com.fasterxml.jackson.databind.ObjectMapper
import io.dropwizard.Configuration
import io.dropwizard.cli.Command
import io.dropwizard.setup.Bootstrap
import net.sourceforge.argparse4j.inf.{Namespace, Subparser}
import org.apache.http.{HttpEntity, HttpResponse}
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.{HttpMultipartMode, MultipartEntityBuilder}
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.impl.client.HttpClients

class CliUnzip() extends Command("list-zip", "lists a zip file") {
  override def configure(subparser: Subparser): Unit = subparser.addArgument("file")
    .`type`(classOf[File])
    .required(false)
    .nargs(1)

  @throws[Exception]
  override def run(bootstrap: Bootstrap[_], namespace: Namespace): Unit = {
    val path = namespace.getString("file")
    if (null == path) throw new IllegalArgumentException("no file, despite default")
    val file = new File(path)
    try {
      val client = HttpClients.createDefault
      try {
        val builder = MultipartEntityBuilder.create.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).addPart("file", new FileBody(file, ContentType.DEFAULT_BINARY))
        val post = new HttpPost("http://localhost:8080/api/unzip")
        post.setEntity(builder.build)
        val response = client.execute(post)
        val responseCode = response.getStatusLine.getStatusCode
        if (200 != responseCode || !contentTypeIsJson(response.getEntity)) {
          showError(response)
          return
        }
        val entries = new ObjectMapper().readValue(response.getEntity.getContent, classOf[Array[String]])
        System.out.println("The file contains these entries:")
        for (entry <- entries) {
          System.out.println(" * " + entry)
        }
      } finally if (client != null) client.close()
    }
  }

  @throws[IOException]
  private def showError(response: HttpResponse) = {
    System.err.println("An error occurred. Please check the server's logs for more information.")
    System.err.println()
    val entity = response.getEntity
    if (contentTypeIsJson(entity)) {
      val root = new ObjectMapper().readTree(entity.getContent)
      if (root.has("message")) System.err.println("The server says: " + root.get("message").textValue)
      else System.err.println("The server said something: " + root)
    }
    else System.err.println("The server's response was not understood, response code: " + response.getStatusLine.getStatusCode)
  }

  private def contentTypeIsJson(entity: HttpEntity): Boolean = {
    for (element <- entity.getContentType.getElements) {
      if ("application/json" == element.getName) return true
    }
    false
  }
}
