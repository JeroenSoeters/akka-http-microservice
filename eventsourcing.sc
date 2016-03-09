import java.util.UUID
import akka.http.scaladsl.model.DateTime
import domain.Aggregate
import domain.batch._
object EventSourcing {

  def makeHandler[TState, TEvent <: AnyRef, TCommand](aggregate: Aggregate[TState, TEvent, TCommand])(load: (Class[_], UUID) => Seq[AnyRef], commit: (UUID, Int, AnyRef) => Unit)(implicit ev: Manifest[TEvent]) =
    (id: UUID, version: Int, command: TCommand) => {


      val events = load(ev.runtimeClass, id) map (_.asInstanceOf[TEvent])
      val state = events.foldLeft(aggregate.zero)(aggregate.apply)
      val event = aggregate.exec(state, command)
      event match {
        case Right(event) =>
          commit(id, version, event)
          Right(event)
        case Left(error) => Left(error)
      }
    }

  val id = UUID.randomUUID()

  val handleCommand = makeHandler(Batch)(
    (_, _) => Seq(Created(id, "sku1", 100, PreProduction), Produced(id, DateTime.now)),
    (id, version, event) => println(s"Storing event ${id} with version ${version}\n${event}"))
  handleCommand(id, 0, MarkAsQaApproved(id))
}