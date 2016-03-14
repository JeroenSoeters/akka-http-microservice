package adapters.http.endpoints

import adapters.http.payloads.ServiceHealthResponse
import adapters.http.serialization.AdminJsonSupport
import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.typesafe.config.Config

import scala.concurrent.ExecutionContextExecutor

/**
  * Created by jeroensoeters on 3/12/16.
  */
trait AdminEndpoint extends AdminJsonSupport {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  def config: Config
  val logger: LoggingAdapter

  def health(): ServiceHealthResponse = ServiceHealthResponse("healthy")

  val adminRoutes =
    path("health") {
      get {
        complete {
          health
        }
      }
    }
}
