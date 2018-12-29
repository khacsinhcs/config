package com.alab.conf

trait HasType {

  def ?(name: String): Option[Field[_]]

  def f[T](field: Field[T]): Field[T]

  def f[T](name: String, label: String, required: Boolean, dataType: DataType[T]): Field[T] = f[T](new Field[T](name, label, required, dataType))

  def f[T](name: String, label: String, dataType: DataType[T]): Field[T] = f[T](name, label, required = true, dataType)

  def f[T](name: String, dataType: DataType[T]): Field[T] = f[T](name, name, dataType)

  def fk[T](field: FK[T]): FK[T]

  def fk[T](name: String, label: String, required: Boolean, dataType: DataType[T], kind: Type): FK[T] = fk[T](new FK[T](name, label, required, dataType, kind))

  def fk[T](name: String, label: String, dataType: DataType[T], kind: Type): FK[T] = fk[T](name, label, required = true, dataType, kind)

  def fk[T](name: String, dataType: DataType[T], kind: Type): FK[T] = fk[T](name, name, dataType, kind)
}

class Type(val name: String, description: String) extends HasType {

  import scala.collection.mutable

  private val mapFields: mutable.Map[String, Field[_]] = mutable.Map()

  TypeSystem ++ this


  override def ?(name: String): Option[Field[_]] = mapFields.get(name)

  def f[T](field: Field[T]): Field[T] = {
    mapFields.put(field.name, field)
    field
  }

  override def fk[T](fk: FK[T]): FK[T] = {
    mapFields.put(fk.name, fk)
    fk
  }
}


trait DataType[T] {
  def toString(t: T): String

  def fromString(str: String): T

  def display(value: T, format: String): String

  def validate(value: T): Option[String]
}

trait StringType extends DataType[String] {
  override def toString(t: String): String = t

  override def fromString(str: String): String = str
}

object StringType extends StringType {
  override def display(value: String, format: String): String = value

  override def validate(value: String): Option[String] = None
}

object EmailType extends StringType {

  import scala.util.matching.Regex

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

  override def validate(value: String): Option[String] = {
    value match {
      case null => Some("Key can't null")
      case someString if someString.trim == "" => Some("Don't allow empty")
      case _ => None
    }
  }
}

class NumberType extends DataType[Double]{
  override def toString(t: Double): String = String.valueOf(t)

  override def fromString(str: String): Double = str.toDouble

  override def display(value: Double, format: String): String = toString(value)

  override def validate(value: Double): Option[String] = None
}

class IntegerType extends DataType[Int] {
  override def toString(t: Int): String = String.valueOf(t)

  override def fromString(str: String): Int = str.toInt

  override def display(value: Int, format: String): String = toString()

  override def validate(value: Int): Option[String] = None
}

object IntType extends IntegerType
object IdKey extends IntegerType