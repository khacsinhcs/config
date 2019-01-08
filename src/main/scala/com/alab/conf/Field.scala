package com.alab.conf

trait Field[T] {
  def name: String

  def label: String

  def required: Boolean

  def dataType: DataType[T]
}

case class NormalField[T](name: String, label: String, required: Boolean, dataType: DataType[T]) extends Field[T]

/**
  * Foreign key
  */
case class FK[T](name: String, label: String, required: Boolean, dataType: DataType[T], ref: Type) extends Field[T] {
  def dot[FieldType](mergeLabels: Boolean, field: Field[FieldType]): FieldPath[FieldType] = FieldPath(mergeLabels, Array(this), field)

  def /[FieldType](field: Field[FieldType]) : FieldPath[FieldType] = dot(mergeLabels = false, field)
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
}