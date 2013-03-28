import org.scalatra.test.specs2._

class NurblizerSpec extends MutableScalatraSpec {
  addServlet(classOf[Nurblizer], "/*")
  
  "GET / on Nurblizer" should {
    "return status 200" in {
      get("/") {
        status must_== 200
      }
    }
  }

  "POST /nurble on Nurblizer with no text" should {
    "return status 400" in {
      post("/nurble") {
        status must_== 400
      }
    }
  }

  "POST /nurble on Nurblizer with valid text" should {
    "return status 200" in {
      post("/nurble", Map("text" -> "Hi there")) {
        status must_== 200
      }
    }
  } 
}
