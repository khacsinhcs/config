package com.alab.model

import com.alab.conf.Field

import scala.collection.mutable

trait HasValues {
  def ~>[T](field: Field[T]): Option[T]

  def demand[T](field: Field[T]) : T = {
    this ~> field match {
      case Some(t) => t
    }
  }

  def get[T](field: Field[T], t: T): T = {
    this ~> field match {
      case Some(value) => value
      case None => t
    }
  }
}


class MapValues(values : Map[String, _]) extends HasValues {

  override def ~>[FieldType](field: Field[FieldType]): Option[FieldType] = {
    values.get(field.name) match {
      case None => None
      case Some(str: String) => Some(field.dataType.fromString(str))
      case Some(t) => Some(t.asInstanceOf[FieldType])
    }
  }

}