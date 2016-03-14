package unit.adapters.http

import java.util.UUID

import adapters.http.endpoints.BatchEndpoint
import adapters.persistence.inmemory.InMemoryEventStore
import akka.event.NoLogging
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import domain.batch.PreProduction
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

/**
  * Created by jeroensoeters on 3/12/16.
  */
class BatchSpec extends FlatSpec
  with Matchers
  with ScalatestRouteTest
  with BeforeAndAfterAll
  with BatchEndpoint {

  override def testConfigSource = "akka.loglevel = WARNING"
  override def config = testConfig
  override val logger = NoLogging

  val createdBatchId = UUID.randomUUID()

  override def beforeAll() =
    InMemoryEventStore.commit(createdBatchId, 0, domain.batch.Created(createdBatchId, "12345", 1000, PreProduction))

  "The manufacturing service" should "create batches" in {
    val batch = "{\"sku\":\"sku1\",\"volume\":500}"

    Post(s"/batch", HttpEntity(`application/json`, batch)) ~> batchRoutes ~> check {
      status shouldBe OK
      val locationHeader = header("Location")
      locationHeader.isDefined should be(true)
      locationHeader.get.value should startWith("/batch/")
    }
  }

  "The manufacturing service" should "reject batches" in {
    val reject = "{\"by\":\"Jeroen\"}"

    Post(s"/batch/${createdBatchId.toString}/commands/reject", HttpEntity(`application/json`, reject)) ~> batchRoutes ~> check {
      status shouldBe OK

      // possibly check read model for updated batch status, for now this is covered in functional test
    }
  }
}
