package domain.batch

import java.util.UUID

sealed trait Command
case class Create(id: UUID = UUID.randomUUID(), sku: String, volume: Int) extends Command
case class Reject(id: UUID, by: String) extends Command
