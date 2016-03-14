import java.util.UUID

object Worksheet {

  val id = UUID.randomUUID()
  val location = "/batch/" + id

  val extracted = location.substring(location.lastIndexOf("/") + 1)
}