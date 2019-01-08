package com.alab.model

import com.alab.conf._

trait HasValues {
  protected def _get[T](field: Field[T]): Option[T]

  def ~>[T](field: Field[T]): Option[T] = _get(field)

  def demand[T](field: Field[T]): T = {
    this ~> field match {
      case Some(t) => t
      case None => throw new IllegalStateException(s"$field is demand")
    }
  }

  def get[T](field: Field[T], t: T): T = {
    this ~> field match {
      case Some(value) => value
      case None => t
    }
  }

  def toString(t: Type): String = {
    t.fields.flatMap(f => {
      _get(f) match {
        case None => None
        case Some(v) => Some(f.name + ": " + v.toString)
      }
    }).mkString(t.n + "(", ", ", ")")
  }
}


class MapValues(private val values: Map[String, _]) extends HasValues {

  override protected def _get[FieldType](field: Field[FieldType]): Option[FieldType] = {

    field match {
      case f: NormalField[FieldType] => getFromMap(f.name, f.dataType)
      case fk: FK[FieldType] => getFromMap(fk.name, fk.dataType)
      case fp: FieldPath[FieldType] => ???
    }

  }

  def getFromMap[FieldType](key: String, dataType: DataType[FieldType]): Option[FieldType] = {
    values.get(key) match {
      case None => None
      case Some(str: String) => Some(dataType.fromString(str))
      case Some(_: FieldType) =>
    }
  }
}