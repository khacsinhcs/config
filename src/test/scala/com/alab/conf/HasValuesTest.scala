package com.alab.conf

import com.alab.model.{Student, Teacher}
import com.alab.model.{HasValues, MapValues}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.HashMap

class HasValuesTest extends FlatSpec with Matchers {

  def initStudent : HasValues = {
    new MapValues(HashMap(
      "first_name" -> "Sinh",
      "age" -> "18"
    ))
  }
  it should "get value from Map" in {
    val student = initStudent

    import com.alab.model.Student._
    student ~> first_name should be(Some("Sinh"))
    student ~> last_name should be(None)
    student ~> age should be(Some(18))
    student demand first_name should be("Sinh")

    the [IllegalStateException] thrownBy {
      student demand last_name
    } should have message "Field(name: \"last_name\", label: \"Last name\", required: true) is demand"
  }

  it should "to string has value" in {
    val student = initStudent
    val str = student toString Student
    println(str)
    str should include ("Student")
  }

  "Field path" should "have a correct name" in {
    val field = Student.teacher.dot(mergeLabels = false,Teacher.first_name)
    field.name should include ("$teacher.first_name")
    field.label should include ("First name")
  }
}
