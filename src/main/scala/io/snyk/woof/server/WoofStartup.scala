package io.snyk.woof.server

object WoofStartup {
  @throws[Exception]
  def main(args: Array[String]): Unit = new WoofApplication().run()
}
