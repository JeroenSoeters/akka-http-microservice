package endpoints

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}
import spray.json.DefaultJsonProtocol

import scala.concurrent.{ExecutionContextExecutor, Future}

case class ServiceHealth(status: String)

trait Protocols extends DefaultJsonProtocol {
  implicit val serviceHealthFormat = jsonFormat1(ServiceHealth.apply)
}

trait Service extends Protocols {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  def config: Config
  val logger: LoggingAdapter

  def health: Future[ServiceHealth] =
    Future.successful(ServiceHealth("healthy"))

  val routes = {
    logRequestResult("scala-microservice") {
      pathPrefix("health") {
        get {
          complete {
            health.map[ToResponseMarshallable] {

            }
          }
        }
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
