package domain

import akka.http.scaladsl.model.DateTime
import java.util.UUID

sealed trait Event
case class Created(id: UUID, sku: String, volume: Int, status: BatchStatus) extends Event
case class Produced(id: UUID, produced: DateTime) extends Event
case class QaApproved(id: UUID, approved: DateTime, by: String) extends Event

sealed trait Command
case class Create(id: UUID, sku: String, volume: Int) extends Command
case class MarkAsProduced(id: UUID) extends Command
case class MarkAsQaApproved(id: UUID) extends Command

sealed trait BatchStatus
case object PreProduction extends BatchStatus
case class AwaitingQa(produced: DateTime) extends BatchStatus
case class Approved(approved: DateTime, by: String) extends BatchStatus

case class Batch(id: UUID, sku: String, volume: Int, status: BatchStatus)

object Batch extends Aggregate[Batch, Event, Command] {

  override def apply(batch: Batch, event: Event): Batch = {
    event match {
      case Created(id, sku, volume, status) => batch.copy(id, sku, volume, status)
      case Produced(id, produced) => batch.copy(status = AwaitingQa(produced))
      case QaApproved(id, approved, by) => batch.copy(status = Approved(approved, by))
    }
  }

  override def exec(batch: Batch, command: Command): Either[String, domain.Event] = {
    command match {
      case Create(id, sku, volume) => Right(Created(id, sku, volume, PreProduction))
      case MarkAsProduced(id) => Right(Produced(id, DateTime.now))
      case MarkAsQaApproved(id) => Right(QaApproved(id, DateTime.now, "Jeroen"))
    }
  }

  override def zero = Batch(null, "", 0, null)
}


