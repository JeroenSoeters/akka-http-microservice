package adapters.http.serialization

import adapters.http.payloads.{RejectBatchRequest, CreateBatchRequest}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import domain.batch.Rejected
import spray.json.DefaultJsonProtocol

/**
  * Created by jeroensoeters on 3/12/16.
  */
trait BatchJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val createBatchFormat   = jsonFormat2(CreateBatchRequest)
  implicit val rejectBatchFormat   = jsonFormat1(RejectBatchRequest)
//  implicit val batchRejectedFormat = jsonFormat3(Rejected)
}
