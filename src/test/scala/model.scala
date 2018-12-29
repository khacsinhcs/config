import org.scalatest.{FlatSpec, Matchers}

trait HasName extends HasType {
  val first_name: Field[String] = f("first_name", "First name", StringType)
  val last_name: Field[String] = f("last_name", "Last name", StringType)
}

trait HasPhone extends HasType {
  val phone: Field[String] = f("phone", "Phone number", required = false, PhoneType)
}

trait HasContactInfo extends HasName with HasPhone

object Student extends Type(name = "Student", description = "Student") with HasContactInfo {
  val age: Field[String] = f("age", StringType)
  val teacher: FK[String] = fk("teacher", StringType, Teacher)
}

object Teacher extends Type(name = "Teacher", description = "He teach student") with HasContactInfo



class Test extends FlatSpec with Matchers {

  "Teacher" should "get front type system" in {
    Teacher // scala class loader
    TypeSystem ? "Teacher" should be (Some(Teacher))
  }

  "First name field" should "get front type" in {
    Teacher ? "first_name" should be (Some(Teacher.first_name))
  }

}
