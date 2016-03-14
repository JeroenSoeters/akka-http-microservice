package adapters.http.payloads

import java.util.UUID

/**
  * Created by jeroensoeters on 3/13/16.
  */
final case class BatchLostToQa(id: UUID, volume: Int)
