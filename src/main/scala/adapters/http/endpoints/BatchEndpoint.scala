package adapters.http.endpoints


import adapters.http.payloads.{CreateBatchRequest, RejectBatchRequest}
import adapters.http.serialization.BatchJsonSupport
import adapters.messaging.kafka.KafkaProducer
import adapters.persistence.inmemory.InMemoryEventStore
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.Config
import domain.batch.{Created, _}
import ports.CommandHandlers


/**
  * Created by jeroensoeters on 3/12/16.
  */
trait BatchEndpoint extends BatchJsonSupport {

  def config: Config

  val logger: LoggingAdapter

  val handleCommand = CommandHandlers.makeHandler(Batch)(InMemoryEventStore.load, InMemoryEventStore.commit)

  val batchRoutes =
    path("batch") {
      post {
        entity(as[CreateBatchRequest]) { batch =>
          val command = Create(sku = batch.sku, volume = batch.volume)
          handleCommand(command.id, 0, command) match {
            case Right(event: Created) =>
              val locationHeader = Location(s"/batch/${event.id}")
              complete(HttpResponse(OK, headers = List(locationHeader)))
            case Left(error) => complete(HttpResponse(BadRequest, entity = error))
          }
        }
      }
    } ~
    path("batch" / JavaUUID / "commands" / "reject") { id =>
      post {
        entity(as[RejectBatchRequest]) { reject =>
          val command = Reject(id = id, by = reject.by)
          handleCommand(command.id, 1, command) match {
            case Right(event: Rejected) =>
              // this is dangerous to do this without a transaction or in a "polling fashion"
              val batchLostToQa: String = "{\"id\":\"" + event.id + "\",\"volume\":" + event.volume + "}"
              KafkaProducer.publish(batchLostToQa, "manufacturing")
              complete(OK)
            case Left(error) =>
              complete(HttpResponse(BadRequest, entity = error))
          }
        }
      }
    }
}
