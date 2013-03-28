import org.scalatra._
import scalate.ScalateSupport
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new Nurblizer, "/*")
  }
}

class Nurblizer extends ScalatraServlet with ScalateSupport {

  lazy val nouns = scala.io.Source.fromFile("src/main/webapp/nouns.txt").getLines.toSeq

  private def nurble(text: String): List[String] = {
    val words = """\b\p{L}+\b""".r.findAllIn(text.toLowerCase).toList
    words.map { w =>
      if (nouns.contains(w)) w.toUpperCase
      else """<span class="nurble">nurble</span>"""
    }
  }

  before() { 
    contentType = "text/html" 
  }

  get("/") {
    scaml("/index")
  }

  post("/nurble") {
    val text = params.getOrElse("text", halt(400))
    scaml("/nurble", "text" -> nurble(text).mkString(" "))
  }

  notFound {
    // remove content type in case it was set through an action
    contentType = null 
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound() 
  }
}