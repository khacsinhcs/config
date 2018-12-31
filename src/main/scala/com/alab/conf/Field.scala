package com.alab.conf

class Field[T](val name: String, val label: String, val required: Boolean, val dataType: DataType[T]) {
  override def toString: String = s"Field($name, $label)"
}

/**
  * Foreign key
  */
class FK[T](override val name: String,
            override val label: String,
            override val required: Boolean,
            override val dataType: DataType[T],
            val ref: Type)
  extends Field[T](name, label, required, dataType)


class FieldPath[T](
                   override val required: Boolean,
                   override val dataType: DataType[T], fks: FK[_])
  extends Field[T]("", "", required, dataType) {
  override val name: String = {
    ""
  }
}