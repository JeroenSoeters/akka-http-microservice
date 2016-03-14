package adapters.http.serialization

import adapters.http.payloads.ServiceHealthResponse
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

/**
  * Created by jeroensoeters on 3/12/16.
  */
trait AdminJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val serviceHealthFormat = jsonFormat1(ServiceHealthResponse)
}
