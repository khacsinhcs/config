package com.alab.model

import com.alab.conf.validate.{Validate, ValidateFail, ValidateSuccess}
import com.alab.conf.{HasValuesType, _}
import com.alab.{Mappable, MappableHelper}

trait HasValues {
  self =>

  def kind: Option[Type] = List(guess(_kind), guess(self getOption("kind", StringType))).find(o => o.isDefined) match {
    case None => None
    case Some(value) => value
  }

  def ? : Validate[List[String]] = kind match {
    case Some(t) => validate(t)
    case None => ValidateSuccess()
  }

  protected var _kind: Option[String] = None

  def is(s: String): HasValues = {
    _kind = Some(s)
    this
  }

  def is(t: Type): HasValues = {
    _kind = Some(t.n)
    this
  }

  def apply[T](f: Field[T]): Option[T] = self -> f

  def ->[T](field: Field[T]): Option[T] = {

    field match {
      case fp: FieldPath[T] =>
        getOption(fp.name, fp.dataType) match {
          case Some(value) => Some(value)
          case None => getOption(fp.head.name, HasValuesType) match {
            case Some(child: HasValues) => child -> fp.child
            case None => None
          }
        }
      case _ => field.dataType.getOption(self, field.name)
    }
  }

  private def getOption[T](name: String, dataType: DataType[T]): Option[T] = {
    dataType.getOption(self, name)
  }

  def getRaw(name: String): Option[_]

  def demand[T](field: Field[T]): T =
    this -> field match {
      case Some(t) => t
      case None => throw new IllegalStateException(s"$field is demand")
    }

  def get[T](field: Field[T], t: T): T =
    this -> field match {
      case Some(value) => value
      case None => t
    }

  def validate(implicit t: Type): Validate[List[String]] = {
    t.fields.map(f => f ? self)
      .foldLeft(List[String]())((ls: List[String], result: Validate[List[String]]) =>
        result match {
          case ValidateSuccess() => ls
          case ValidateFail(xs) => ls ++ xs
        })
    match {
      case ls if ls.isEmpty => ValidateSuccess()
      case ls => ValidateFail(ls)
    }
  }

  private def guess(sType: Option[String]): Option[Type] = sType match {
    case Some(s) => TypeSystem / s
    case None => None
  }


  def toString(implicit t: Type): String =
    t.fields.flatMap(f => {
      ->(f) match {
        case None => None
        case Some(v) => Some(f.name + ": " + v.toString)
      }
    }).mkString(t.n + "(", ", ", ")")


  def +:(that: HasValues): HasValues = (name: String) => self.getRaw(name) match {
    case Some(t) => Some(t)
    case None => that.getRaw(name)
  }

  def :+(that: HasValues): HasValues = that +: self

  def materialize[T: HasValuesMapper](implicit kind: Type): T = HasValuesMapperHelper.materialize[T](self, kind)
}


object HasValues {
  def from[T: Mappable](t: T): HasValues = MapValues(MappableHelper.mapify[T](t))

}

case class MapValues(private val values: Map[String, _]) extends HasValues {
  override def getRaw(name: String): Option[_] = values.get(name) match {
    case Some(Some(t)) => Some(t)
    case other => other
  }
}

object EmptyValues extends HasValues {
  override def getRaw(name: String): Option[_] = None
}