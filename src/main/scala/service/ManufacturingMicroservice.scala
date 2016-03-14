package service

import adapters.http.endpoints.{AdminEndpoint, BatchEndpoint}
import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

/**
  * Created by jeroensoeters on 3/12/16.
  */
object ManufacturingMicroservice extends App
  with AdminEndpoint
  with BatchEndpoint {

  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  Http().bindAndHandle(adminRoutes ~ batchRoutes, config.getString("http.interface"), config.getInt("http.port"))
}
