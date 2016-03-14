package domain.batch

import akka.http.scaladsl.model.DateTime
import java.util.UUID

import domain.Aggregate



sealed trait BatchStatus
case object PreProduction extends BatchStatus
case class QaRejected(by: String, when: DateTime) extends BatchStatus

case class Batch(id: UUID, sku: String, volume: Int, status: BatchStatus)

object Batch extends Aggregate[Batch, Event, Command] {

  override def apply(batch: Batch, event: Event): Batch = {
    event match {
      case Created(id, sku, volume, status) => batch.copy(id, sku, volume, status)
      case Rejected(id, by, when, _) => batch.copy(status = QaRejected(by, when))
    }
  }

  override def exec(batch: Batch, command: Command): Either[String, Event] = {
    command match {
      case Create(id, sku, volume) => Right(Created(id, sku, volume, PreProduction))
      case Reject(id, by)          => Right(Rejected(id, by, DateTime.now, batch.volume))
    }
  }

  override def zero = Batch(null, "", 0, null)
}


