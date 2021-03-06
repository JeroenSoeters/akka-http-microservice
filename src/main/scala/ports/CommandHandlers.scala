package ports

import java.util.UUID

import domain.Aggregate

/**
  * Created by jeroensoeters on 3/8/16.
  */
object CommandHandlers {

  def makeHandler[TState, TEvent <: AnyRef, TCommand](aggregate: Aggregate[TState, TEvent, TCommand])
                                                     (load: (Class[_], UUID) => Seq[AnyRef],
                                                      commit: (UUID, Int, AnyRef) => Unit)(implicit ev: Manifest[TEvent]) =
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

}
