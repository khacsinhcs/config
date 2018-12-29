class Field[T](val name: String, val label: String, val required: Boolean, val dataType: DataType[T])

class FK[T](override val name: String,
            override val label: String,
            override val required: Boolean,
            override val dataType: DataType[T],
            val ref: Type)
  extends Field[T](name, label, required, dataType) {
}