package unit.adapters.http

import adapters.http.endpoints.AdminEndpoint
import adapters.http.payloads.ServiceHealthResponse
import akka.event.NoLogging
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest._

class AdminSpec extends FlatSpec with Matchers with ScalatestRouteTest with AdminEndpoint {
  override def testConfigSource = "akka.loglevel = WARNING"
  override def config = testConfig
  override val logger = NoLogging

  "The service" should "be healthy" in {

      Get(s"/health") ~> adminRoutes ~> check {
        status shouldBe OK
        contentType shouldBe `application/json`
        responseAs[ServiceHealthResponse] should be(ServiceHealthResponse("healthy"))
      }
  }
}
