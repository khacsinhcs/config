package com.alab.conf

import com.alab.conf.validate.{Validate, ValidateFail, ValidateSuccess, Validator}
import com.alab.model.HasValues

trait Field[T] extends (HasValues => Option[T]) {
  self =>

  private var validators: List[Validator[T, String]] = List[Validator[T, String]]()

  def name: String

  def label: String

  def required: Boolean

  def dataType: DataType[T]

  def is(v: Validator[T, String]): Field[T] = {
    validators = validators :+ v
    self
  }

  def ?(data: HasValues): Validate[List[String]] = {
    data -> this match {
      case Some(value: T) => this ? value
      case None => ValidateSuccess()
    }
  }

  def ?(t: T): Validate[List[String]] =
    validators.map(validate => validate.apply(t)).foldLeft(List[String]())((ls: List[String], result: Validate[String]) => result match {
      case ValidateSuccess() => ls
      case ValidateFail(s) => ls :+ s
    }) match {
      case ls if ls.isEmpty => ValidateSuccess()
      case ls => ValidateFail(ls)
    }

  override def apply(v1: HasValues): T = v1 -> self
}

case class NormalField[T](name: String, label: String, required: Boolean, dataType: DataType[T]) extends Field[T]

/**
  * Foreign key
  */
case class FK[T](name: String, label: String, required: Boolean, dataType: DataType[T], ref: Type) extends Field[T] {
  def dot[FieldType](mergeLabels: Boolean, field: Field[FieldType]): FieldPath[FieldType] = FieldPath(mergeLabels, Array(this), field)

  def /[FieldType](field: Field[FieldType]): FieldPath[FieldType] = dot(mergeLabels = false, field)
}

case class FieldPath[T](mergeLabels: Boolean, paths: Array[FK[_]], leaf: Field[T])
  extends Field[T] {
  override val label: String = if (mergeLabels) paths.apply(paths.length - 1).label else leaf.label

  private lazy val _name = paths.map(f => "$" + f.name).mkString("", ".", ".") + leaf.name

  override def name: String = _name

  override def required: Boolean = leaf.required

  override def dataType: DataType[T] = leaf.dataType

  def /[FT](field: Field[FT]): FieldPath[FT] = {
    leaf match {
      case f: FK[T] => FieldPath(mergeLabels, paths :+ f, field)
    }
  }

  def child: Field[T] = if (paths.length == 1) leaf else FieldPath(mergeLabels, paths.tail, leaf)

  def head: FK[_] = paths.length match {
    case l if l > 0 => paths.head
  }
}