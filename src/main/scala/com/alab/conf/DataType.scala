package com.alab.conf

import com.alab.conf.validate.{Validate, ValidateFail, ValidateSuccess}
import com.alab.model.{HasValues, MapValues}

import scala.reflect.ClassTag


trait DataType[T] extends Immutable {
  implicit val classTag: ClassTag[T]

  def toString(t: T): String

  def fromString(str: String): T

  def display(value: T, format: String): String

  def validate(value: T): Validate[String]

  def getOption(values: HasValues, name: String): Option[T] = cast(values.getRaw(name))

  def cast(value: Option[_]): Option[T]

  def listType: ListType[T] = new ListType[T](this)
}

trait StringType extends DataType[String] {
  override def toString(t: String): String = t

  override def fromString(str: String): String = str

  override implicit val classTag: ClassTag[String] = implicitly[ClassTag[String]]

  override def cast(value: Option[_]): Option[String] = value match {
    case Some(s: String) => Some(s)
    case Some(x) => Some(x.toString)
    case _ => None
  }
}

object StringType extends StringType {
  override def display(value: String, format: String): String = value

  override def validate(value: String): Validate[String] = ValidateSuccess()
}

object EmailType extends StringType {

  import scala.util.matching.Regex

  private val regex: Regex = """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r

  override def validate(value: String): Validate[String] =
    value match {
      case null => ValidateFail("Email can't null")
      case mail if mail.trim.isEmpty => ValidateFail("Empty email")
      case mail if regex.findFirstIn(mail).isDefined => ValidateSuccess()
      case _ => ValidateFail("Wrong email format")
    }

  override def display(value: String, format: String): String = value
}

object PhoneType extends StringType {

  import scala.util.matching.Regex

  private val regex: Regex = """""".r

  override def display(value: String, format: String): String = ???

  override def validate(value: String): Validate[String] = ValidateSuccess()

  override def fromString(str: String): String = {
    str match {
      case null => null
    }
  }
}

object StringKey extends StringType {
  override def display(value: String, format: String): String = value

  override def validate(value: String): Validate[String] =
    value match {
      case null => ValidateFail("Key can't null")
      case someString if someString.trim == "" => ValidateFail("Don't allow empty")
      case _ => ValidateSuccess()
    }
}

object NumberType extends DataType[Double] {
  override implicit val classTag: ClassTag[Double] = implicitly[ClassTag[Double]]


  override def cast(value: Option[_]): Option[Double] = value match {
    case Some(d: Double) => Some(d)
    case Some(s: String) => Some(fromString(s))
    case Some(d: Int) => Some(d)
    case Some(f: Float) => Some(f.toDouble)
    case _ => None
  }

  override def toString(t: Double): String = String.valueOf(t)

  override def fromString(str: String): Double = str.toDouble

  override def display(value: Double, format: String): String = toString(value)

  override def validate(value: Double): Validate[String] = ValidateSuccess()

}

class IntegerType extends DataType[Int] {

  override def cast(value: Option[_]): Option[Int] = value match {
    case Some(d: Double) => Some(d.intValue())
    case Some(s: String) => Some(fromString(s))
    case Some(d: Int) => Some(d)
    case Some(f: Float) => Some(f.toInt)
    case _ => None
  }

  override implicit val classTag: ClassTag[Int] = implicitly[ClassTag[Int]]

  override def toString(t: Int): String = String.valueOf(t)

  override def fromString(str: String): Int = str.toInt

  override def display(value: Int, format: String): String = toString()

  override def validate(value: Int): Validate[String] = ValidateSuccess()

}

object IntType extends IntegerType

object IdKey extends IntegerType

object HasValuesType extends DataType[HasValues] {
  override implicit val classTag: ClassTag[HasValues] = implicitly[ClassTag[HasValues]]

  override def cast(value: Option[_]): Option[HasValues] = value match {
    case Some(hasValue: HasValues) => Some(hasValue)
    case Some(s: String) => Some(fromString(s))
    case Some(map: Map[String, _]) => Some(MapValues(map))
    case _ => None
  }

  override def toString(t: HasValues): String = ""

  override def fromString(str: String): HasValues = ???

  override def display(value: HasValues, format: String): String = ???

  override def validate(value: HasValues): Validate[String] = ValidateSuccess()
}

class ListType[T](val dataType: DataType[T]) extends DataType[List[T]] {
  override implicit val classTag: ClassTag[List[T]] = implicitly[ClassTag[List[T]]]

  override def fromString(str: String): List[T] = ???

  override def display(value: List[T], format: String): String = toString(value)

  override def toString(t: List[T]): String = t match {
    case List() => "[]"
    case ls => ls.map(item => item.toString).mkString("[", ",", "]")
  }

  override def validate(value: List[T]): Validate[String] = ValidateSuccess()

  override def cast(value: Option[_]): Option[List[T]] = value match {
    case None => None
    case Some(ls: List[T]) => Some(ls)
    case Some(t: T) => Some(List(t))
  }
}