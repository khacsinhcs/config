package com.alab.model

import com.alab.conf.{Field, Type}

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
    }).mkString(t.name + "(", ", ", ")")
  }
}


class MapValues(values: Map[String, _]) extends HasValues {

  override protected def _get[FieldType](field: Field[FieldType]): Option[FieldType] = {
    values.get(field.name) match {
      case None => None
      case Some(str: String) => Some(field.dataType.fromString(str))
      case Some(t) => Some(t.asInstanceOf[FieldType])
    }
  }
}