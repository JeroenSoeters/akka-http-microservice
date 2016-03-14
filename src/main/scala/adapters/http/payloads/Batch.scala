package adapters.http.payloads

/**
  * Created by jeroensoeters on 3/12/16.
  */
final case class CreateBatchRequest(sku: String, volume: Int)

final case class RejectBatchRequest(by: String)
