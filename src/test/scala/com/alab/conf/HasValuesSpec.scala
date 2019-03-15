package com.alab.conf

import com.alab.model.App.{Student, _}
import org.scalatest.{FlatSpec, Matchers}

class HasValuesSpec extends FlatSpec with Matchers {


  "student hasValue" should "get value" in {
    val student = HasValueHelper.createStudent

    import com.alab.model.App.Student._
    student -> first_name should be(Some("Sinh"))
    student -> last_name should be(None)
    student -> age should be(Some(18))
    student demand subjects should have size 2
    println(student demand subjects)
    student demand first_name should be("Sinh")
    student -> (teacher / first_name) should be(Some("Oanh"))
    the[IllegalStateException] thrownBy {
      student demand last_name
    }
  }

  it should "to string has value" in {
    val student = HasValueHelper.createStudent
    val str = student toString Student
    println(str)
    str should include("Student")
  }

  "Field path" should "have a correct name" in {
    val field = Student.teacher / Teacher.faculty / Faculty.name
    field.name should include("$teacher.$faculty.name")
    field.label should include("Name")
  }

  "Get FieldPath value" should "be work" in {
    val facultyName = Student.teacher / Teacher.faculty / Faculty.name
    val student = HasValueHelper.createStudent
    Student.first_name(student) should be(Some("Sinh"))
    student -> facultyName should be(None)
    facultyName(student) should be(None)
  }

  "Get kind" should "from empty context" in {
    val student = HasValueHelper.createStudent
    student.kind should be(None)

    student is Student
    student.kind should be(Some(Student))
  }
}
