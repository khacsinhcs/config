import scala.collection.mutable

class TypeSystem {
  val types : mutable.Map[String, Type] = mutable.Map[String, Type]()

  def ++(t: Type): TypeSystem = {
    types + (t.name -> t)
    this
  }

  def ? (name: String): Option[Type] = {
    types.get(name)
  }
}
