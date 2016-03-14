package functional

import java.util.UUID

import adapters.http.payloads.BatchLostToQa
import adapters.http.serialization.BatchJsonSupport
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model._
import StatusCodes._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.softwaremill.react.kafka.KafkaMessages.StringConsumerRecord
import com.softwaremill.react.kafka.{ConsumerProperties, ReactiveKafka}
import domain.batch.Rejected
import org.apache.kafka.common.serialization.StringDeserializer
import org.reactivestreams.Publisher
import org.scalatest.{ShouldMatchers, WordSpec}

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.parsing.json.JSON

/**
  * Created by jeroensoeters on 3/12/16.
  */
class BatchSpec extends WordSpec
  with ShouldMatchers
  with BatchJsonSupport   {

  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  "When a batch is rejected by QA the manufacturing service" should {

    "publish a batch lost to QA event" in {
      /* Given */

      // Create a batch
      val batchIdOpt = createBatch("sku1", 500)

      // Verify batch is created
      batchIdOpt.isDefined should be(true)

      // Subscribe to Kafka topic
      val events = ListBuffer[String]()

      subscribeToKafkaTopic("manufacturing").runForeach(events += _.value())

      /* When */

      // Reject batch
      val payload = "{\"by\":\"Rajesh\"}"
      val res = Await.result(
        Http().singleRequest(
          HttpRequest(
            POST,
            uri = s"http://192.168.99.100:9000/batch/${batchIdOpt.get}/commands/reject",
            entity = HttpEntity(`application/json`, payload))),
        5 seconds)

      res.status shouldBe OK

      /* Then */

      // this should be a polling method instead of sleeping for a fixed duration
      Thread.sleep(5000)

      // Get last event
      val batchLostToQaOpt = events.lastOption
      batchLostToQaOpt shouldBe defined

      // Parse JSON
      val parsed = JSON.parseFull(batchLostToQaOpt.get)
      parsed shouldBe defined

      // Assert event payload
      val batchLost = parsed.asInstanceOf[BatchLostToQa]
      batchLost.id shouldBe batchIdOpt.get
      batchLost.volume shouldBe 500
    }
  }

  def subscribeToKafkaTopic(topic: String)  =  {
    val kafka = new ReactiveKafka()
    val kafkaHost = "192.168.99.100:9092"

    val publisher: Publisher[StringConsumerRecord] = kafka.consume(ConsumerProperties(
      bootstrapServers = kafkaHost,
      topic = topic,
      groupId = "1234",
      valueDeserializer = new StringDeserializer()
    ))

    Source.fromPublisher(publisher)
  }

  def createBatch(sku: String, volume: Int): Option[UUID] = {
    def extractId(location: String): UUID =
      UUID.fromString(location.substring(location.lastIndexOf("/") + 1))

    val payload = "{\"sku\":\"" + sku + "\",\"volume\":" + volume + "}"
    val res = Await.result(
      Http().singleRequest(
        HttpRequest(
          POST,
          uri = "http://192.168.99.100:9000/batch",
          entity = HttpEntity(`application/json`, payload))),
      5 seconds)

    res.getHeader("Location").map(_.value()).map(extractId)
  }
}
