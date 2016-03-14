package adapters.persistence.inmemory

import java.util.UUID

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by jeroensoeters on 3/12/16.
  */
object InMemoryEventStore {

  val data = mutable.HashMap.empty[UUID, ListBuffer[AnyRef]]

  def load(aggregateType: Class[_], aggregateId: UUID): Seq[AnyRef] =
    if (data.contains(aggregateId)) data(aggregateId) else Seq.empty[AnyRef]

  def commit(aggregateId: UUID, version: Int, event: AnyRef): Unit =
    data.contains(aggregateId) match {
      case true  => data.get(aggregateId) map(_ += event)
      case false => data.put(aggregateId, ListBuffer(event))
    }
}
