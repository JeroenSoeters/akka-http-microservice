package domain.batch

import java.util.UUID

sealed trait Command
case class Create(id: UUID, sku: String, volume: Int) extends Command
case class MarkAsProduced(id: UUID) extends Command
case class MarkAsQaApproved(id: UUID) extends Command
