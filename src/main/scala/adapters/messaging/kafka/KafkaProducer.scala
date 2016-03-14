package adapters.messaging.kafka

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.softwaremill.react.kafka.{ProducerMessage, ProducerProperties, ReactiveKafka}
import org.apache.kafka.common.serialization.StringSerializer

/**
  * Created by jeroensoeters on 3/13/16.
  */
object KafkaProducer {

  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val kafka = new ReactiveKafka()

  def publish(message: String, topic: String) = {
    Thread.sleep(500)
    Source.
      fromIterator(() => Iterator("yaaay, an event!!!!!")).
      map(ProducerMessage(_)).
      to(Sink.fromSubscriber(createSubscriber(topic))).
      run()
  }

  private def createSubscriber(topic: String) =
    kafka.publish(ProducerProperties(
      bootstrapServers = "192.168.99.100:9092",
      topic = "manufacturing",
      valueSerializer = new StringSerializer()))
}
