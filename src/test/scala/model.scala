
implicit val typeSystem = new TypeSystem()

trait HasName extends HasType {
  val first_name = f("first_name", "First name", StringType)
  val last_name = f("last_name", "Last name", StringType)
}

trait HasPhone extends HasType {
  val phone = f("phone", "Phone number", required = false, PhoneType)
}

trait HasContactInfo extends HasName with HasPhone

object Student extends Type(name = "Student", description = "Student") with HasContactInfo {
  val age = f("age", StringType)
}

object Teacher extends Type(name = "Teacher", description = "He teach student") with HasContactInfo {
}


def main(args: Array[String]): Unit = {
  val teacher = typeSystem ? "Teacher"
  if (teacher isDefined) {
    def tTeacher = teacher.get

    val field: String = tTeacher ? "first_name" match {
      case None => "Something go wrong"
      case Some(f) => f.name
    }

    print(field)

  }

}