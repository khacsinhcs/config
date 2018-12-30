import com.alab.conf.TypeSystem
import org.scalatest.{FlatSpec, Matchers}

class TypeSystemTest extends FlatSpec with Matchers {
  Bootstrap allTypes()

  "Teacher" should "get front type system" in {
    TypeSystem ? "Teacher" should be(Some(Teacher))
  }

  "First name field" should "get front type" in {
    Teacher ? "first_name" should be(Some(Teacher.first_name))
  }

}
