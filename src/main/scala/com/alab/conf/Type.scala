package com.alab.conf

trait HasType {
  def apply(name: String): Option[Field[_]] = this ? name

  def ?(name: String): Option[Field[_]]

  def f[T](field: Field[T]): Field[T]

  def f[T](name: String, label: String, required: Boolean, dataType: DataType[T]): Field[T] = f[T](NormalField[T](name, label, required, dataType))

  def f[T](name: String, label: String, dataType: DataType[T]): Field[T] = f[T](name, label, required = true, dataType)

  def f[T](name: String, dataType: DataType[T]): Field[T] = f[T](name, name, dataType)

  def fk[T](field: FK[T]): FK[T]

  def fk[T](name: String, label: String, required: Boolean, dataType: DataType[T], kind: Type): FK[T] = fk[T](new FK[T](name, label, required, dataType, kind))

  def fk[T](name: String, label: String, dataType: DataType[T], kind: Type): FK[T] = fk[T](name, label, required = true, dataType, kind)

  def fk[T](name: String, dataType: DataType[T], kind: Type): FK[T] = fk[T](name, name, dataType, kind)
}

class Type(val n: String, des: String) extends HasType {
  import scala.collection.mutable

  private val mapFields: mutable.Map[String, Field[_]] = mutable.LinkedHashMap()

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

  def fields: Iterable[Field[_]] = mapFields.values

}

