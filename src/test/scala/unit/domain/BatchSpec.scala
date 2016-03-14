package unit.domain

import java.util.UUID

import domain.batch._
import org.scalatest.{BeforeAndAfterEach, ShouldMatchers, WordSpec}
import ports.CommandHandlers

import scala.collection.mutable.ListBuffer

/**
  * Created by jeroensoeters on 3/8/16.
  */
class BatchSpec extends WordSpec
  with ShouldMatchers
  with BeforeAndAfterEach {

  val events = new ListBuffer[AnyRef]
  val handler = CommandHandlers.makeHandler(Batch)(
    (_, _) => events,
    (id, version, event) => events += event
  )

  override def beforeEach() =
    events.clear()

  "The batch command handler" should {

    "commit a batch created event when creating a batch" in {
      val command = Create(UUID.randomUUID(), "sku1", 500)

      val result = handler(command.id, 0, command)

      events.length should be(1)

      val created = events.head.asInstanceOf[Created]
      created.id should be (command.id)
      created.sku should be (command.sku)
      created.status should be(PreProduction)
    }

    "store a rejected event" in {
      val created = Created(UUID.randomUUID(), "some_sku", 500, PreProduction)
      events += created

      val command = Reject(created.id, "Steve")

      val result = handler(command.id, 1, command)

      events.length should be(2)

      val rejected = events.last.asInstanceOf[Rejected]
      rejected.id should be(command.id)
      rejected.by should be("Steve")
    }
  }
}
