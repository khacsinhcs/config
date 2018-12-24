import scala.util.matching.Regex

trait StringType extends DataType[String] {
  override def toString(t: String): String = t

  override def fromString(str: String): String = str
}

object EmailType extends StringType {
  private val regex: Regex = """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r
  override def validate(value: String): Option[String] = {
    value match {
      case null => Some("Email can't null")
      case mail if mail.trim.isEmpty => Some("Empty email")
      case mail if regex.findFirstIn(mail).isDefined => None
      case _ => Some("Wrong email format")
    }
  }
  override def display(value: String, format: String): String = value
}

object PhoneType extends StringType {
  private val regex: Regex = """""".r

  override def display(value: String, format: String): String = ???

  override def validate(value: String): Option[String] = ???

  override def fromString(str: String): String = {
    str match {
      case null => null
    }
  }
}

