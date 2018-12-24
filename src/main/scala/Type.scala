trait  Type[T] {
  def toString(t : T) : String

  def fromString(str : String) : T

  def display(value: T, format: String) : String

  def validate(value: T) : Option[String]
}
