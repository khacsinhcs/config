package com.alab.conf

import com.alab.model.HasValues

import scala.reflect.ClassTag


trait DataType[T] extends Immutable {
  implicit val classTag: ClassTag[T]

  def toString(t: T): String

  def fromString(str: String): T

  def display(value: T, format: String): String

  def validate(value: T): Option[String]

}

trait StringType extends DataType[String] {
  override def toString(t: String): String = t

  override def fromString(str: String): String = str

  override implicit val classTag: ClassTag[String] = implicitly[ClassTag[String]]

}

object StringType extends StringType {
  override def display(value: String, format: String): String = value

  override def validate(value: String): Option[String] = None
}

object EmailType extends StringType {

  import scala.util.matching.Regex

  private val regex: Regex = """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r

  override def validate(value: String): Option[String] =
    value match {
      case null => Some("Email can't null")
      case mail if mail.trim.isEmpty => Some("Empty email")
      case mail if regex.findFirstIn(mail).isDefined => None
      case _ => Some("Wrong email format")
    }

  override def display(value: String, format: String): String = value
}

object PhoneType extends StringType {

  import scala.util.matching.Regex

  private val regex: Regex = """""".r

  override def display(value: String, format: String): String = ???

  override def validate(value: String): Option[String] = ???

  override def fromString(str: String): String = {
    str match {
      case null => null
    }
  }
}

object StringKey extends StringType {
  override def display(value: String, format: String): String = value

  override def validate(value: String): Option[String] =
    value match {
      case null => Some("Key can't null")
      case someString if someString.trim == "" => Some("Don't allow empty")
      case _ => None
    }
}

object NumberType extends DataType[Double] {
  override implicit val classTag: ClassTag[Double] = implicitly[ClassTag[Double]]


  override def toString(t: Double): String = String.valueOf(t)

  override def fromString(str: String): Double = str.toDouble

  override def display(value: Double, format: String): String = toString(value)

  override def validate(value: Double): Option[String] = None

}

class IntegerType extends DataType[Int] {
  override implicit val classTag: ClassTag[Int] = implicitly[ClassTag[Int]]

  override def toString(t: Int): String = String.valueOf(t)

  override def fromString(str: String): Int = str.toInt

  override def display(value: Int, format: String): String = toString()

  override def validate(value: Int): Option[String] = None

}

object IntType extends IntegerType

object IdKey extends IntegerType

class HasValuesType extends DataType[HasValues] {
  override implicit val classTag: ClassTag[HasValues] = implicitly[ClassTag[HasValues]]

  override def toString(t: HasValues): String = ""

  override def fromString(str: String): HasValues = ???

  override def display(value: HasValues, format: String): String = ???

  override def validate(value: HasValues): Option[String] = ???
}