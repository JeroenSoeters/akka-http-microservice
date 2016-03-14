import java.util.UUID
import akka.http.scaladsl.model.DateTime
import domain.batch._
import ports.CommandHandlers

object EventSourcing {

  val id = UUID.randomUUID()

  val handleCommand = CommandHandlers.makeHandler(Batch)(
    (_, _) => Seq(Created(id, "sku1", 100, PreProduction), Produced(id, DateTime.now)),
    (id, version, event) => println(s"Storing event ${id} with version ${version}\n${event}"))
  handleCommand(id, 0, Approve(id))
}