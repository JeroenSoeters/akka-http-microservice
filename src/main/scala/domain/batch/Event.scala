package domain.batch

import java.util.UUID

import akka.http.scaladsl.model.DateTime
import domain.BatchStatus

sealed trait Event
case class Created(id: UUID, sku: String, volume: Int, status: BatchStatus) extends Event
case class Produced(id: UUID, produced: DateTime) extends Event
case class QaApproved(id: UUID, approved: DateTime, by: String) extends Event