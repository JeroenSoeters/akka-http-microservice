package domain

/**
  * Created by jeroensoeters on 3/8/16.
  */
trait Aggregate[TState, TEvent <: AnyRef, TCommand] {
  def zero: TState
  def apply(state: TState, event: TEvent): TState
  def exec(state: TState, command: TCommand): Either[String, TEvent]
}