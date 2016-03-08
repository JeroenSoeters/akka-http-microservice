package endpoints

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}
import spray.json.DefaultJsonProtocol

import scala.concurrent.ExecutionContextExecutor

final case class ServiceHealth(status: String)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val serviceHealthFormat = jsonFormat1(ServiceHealth)
}

trait Service extends JsonSupport {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  def config: Config
  val logger: LoggingAdapter

  def health(): ServiceHealth = ServiceHealth("healthy")

  val routes =
    path("health") {
      get {
        complete {
          health
        }
      }
    }
}

object ScalaMicroservice extends App with Service {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))
}
