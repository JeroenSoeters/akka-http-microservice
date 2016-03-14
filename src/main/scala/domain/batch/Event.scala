package domain.batch

import java.util.UUID

import akka.http.scaladsl.model.DateTime

sealed trait Event
case class Created(id: UUID, sku: String, volume: Int, status: BatchStatus) extends Event
// taking a shortcut here, volume should not be on this rejected event but acquired in a different fashion
// before publishing the external BatchLostToQa event
case class Rejected(id: UUID, by: String, when: DateTime, volume: Int) extends Event