
implicit val typeSystem = new TypeSystem()

trait HasName extends HasType {
  val first_name = f("first_name", "First name", StringType)
  val last_name = f("first_name", "Last name", StringType)
}

object Student extends Type(name = "Student", description = "Student") {
  val first_name = f("first_name", "First name", StringType)
  val last_name = f("first_name", "Last name", StringType)
}

object Teacher extends Type(name = "Teacher", description = "He teach student") {
}