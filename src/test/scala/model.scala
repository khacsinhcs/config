import com.alab.conf._

trait HasName extends HasType {
  val first_name: Field[String] = f("first_name", "First name", StringType)
  val last_name: Field[String] = f("last_name", "Last name", StringType)
}

trait HasPhone extends HasType {
  val phone: Field[String] = f("phone", "Phone number", required = false, PhoneType)
}

trait HasId extends HasType {
  val id: Field[Int] = f("id", "Id", required = true, IdKey)
}

trait HasKeyName extends HasType {
  val key_name: Field[String] = f("key_name", "Key Name", required = true, StringKey)
}

trait HasContactInfo extends HasName with HasPhone

object Student extends Type(name = "Student", description = "Student") with HasId with HasContactInfo {
  val age: Field[String] = f("age", StringType)
  val teacher: FK[String] = fk("teacher", StringKey, Teacher)
}

object Teacher extends Type(name = "Teacher", description = "He teach student") with HasKeyName with HasContactInfo


object Config {
  def bootstrap : Unit = {
    Teacher
    Student
  }
}
import org.scalatest.{FlatSpec, Matchers}

class Test extends FlatSpec with Matchers {
  //Bootstrap object
  Config bootstrap

  "Teacher" should "get front type system" in {
    TypeSystem ? "Teacher" should be(Some(Teacher))
  }

  "First name field" should "get front type" in {
    Teacher ? "first_name" should be(Some(Teacher.first_name))
  }

}
