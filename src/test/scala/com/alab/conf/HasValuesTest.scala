package com.alab.conf

import com.alab.model.App.{Student, _}
import com.alab.model.{HasValues, MapValues}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.HashMap

class HasValuesTest extends FlatSpec with Matchers {

  def createMapValue: HasValues = {
    new MapValues(HashMap(
      "first_name" -> "Sinh",
      "age" -> "18",
      "teacher" -> HashMap("name" -> "Oanh")
    ))
  }

  "student hasValue" should "get value" in {
    val student = createMapValue

    import com.alab.model.App.Student._
    student -> first_name should be(Some("Sinh"))
    student -> last_name should be(None)
    student -> age should be(Some(18))
    student demand first_name should be("Sinh")
    student -> (teacher > first_name) should be("Oanh")
    the[IllegalStateException] thrownBy {
      student demand last_name
    }
  }

  it should "to string has value" in {
    val student = createMapValue
    val str = student toString Student
    println(str)
    str should include("Student")
  }

  "Field path" should "have a correct name" in {
    val field = Student.teacher > Teacher.faculty > Faculty.name
    field.name should include("$teacher.$faculty.name")
    field.label should include("Name")
  }

  "Get FieldPath value" should "be work" in {
    val field = Student.teacher > Teacher.faculty > Faculty.name
    val student = createMapValue
    student -> field should be(None)
  }
}
