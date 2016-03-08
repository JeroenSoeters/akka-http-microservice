package unit.endpoints

import akka.event.NoLogging
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import endpoints.{ServiceHealth, Service}
import org.scalatest._

class AdminSpec extends FlatSpec with Matchers with ScalatestRouteTest with Service {
  override def testConfigSource = "akka.loglevel = WARNING"
  override def config = testConfig
  override val logger = NoLogging

  "Service" should "respond healthy to health check endpoint" in {

    Get(s"/health") ~> routes ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      responseAs[ServiceHealth] should be(ServiceHealth("healthy"))
    }
  }
}
