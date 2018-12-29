# CONFIGURATION LANGUAGE AGAIN?

Many project start configuration by Json, Annotation or Xml. But all these data kind is not code
Then we can't handle many hard case in there.

Scala is a extendable language. It should be the best suitable for configuration language.

What we want to config:
1. Domain object
2. Resource
3. Data Transfer Object

As a Developer, we don't want to have many dummy code. Code should be compact and clean. If we keep 
write dummy code, then we tent to be dummy dev :D

Let's see. What will we do????

Example, we want config domain object like:
```aidl
  import com.alab.conf._
  import com.alab.model.MapValues
  
  import scala.collection.immutable.HashMap
  
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
    val age: Field[Int] = f("age", IntType)
    val teacher: FK[String] = fk("teacher", StringKey, Teacher)
  }
  
  object Teacher extends Type(name = "Teacher", description = "He teach student") with HasKeyName with HasContactInfo
  
  
  object Bootstrap {
    def allTypes(): Unit = {
      Teacher
      Student
    }
  }
  
  import org.scalatest.{FlatSpec, Matchers}
  
  class Test extends FlatSpec with Matchers {
    Bootstrap allTypes()
  
    "Teacher" should "get front type system" in {
      TypeSystem ? "Teacher" should be(Some(Teacher))
    }
  
    "First name field" should "get front type" in {
      Teacher ? "first_name" should be(Some(Teacher.first_name))
    }
  
    it should "get value from Map" in {
      def student = new MapValues(HashMap(
        "first_name" -> "Sinh",
        "age" -> "18"
      ))
  
      import Student._
      student ~> first_name should be(Some("Sinh"))
      student ~> last_name should be(None)
      student ~> age should be(Some(18))
      student demand first_name should be("Sinh")
    }
  
  }

```

