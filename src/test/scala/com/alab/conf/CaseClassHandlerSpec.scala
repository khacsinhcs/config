package com.alab.conf

import com.alab.model.App._
import com.alab.model.{HasValues, HasValuesMapperHelper, MapValues}
import com.alab.{Mappable, MappableHelper}
import org.scalatest.{FlatSpec, Matchers}

class CaseClassHandlerSpec extends FlatSpec with Matchers {

  Bootstrap allTypes()

  import Mappable._

  val stu = StudentClazz(Some("Sinh"), Some("le"), 10)

  case class StudentClazz(firstName: Option[String],
                          lastName: Option[String],
                          age: Int)

  "student" should "mapify" in {
    val map = MappableHelper.mapify(stu)
    map should contain key "firstName"
    val student = MapValues(map)
    student(Student.first_name)

    val newStudent = MappableHelper.materialize[StudentClazz](Map("firstName" -> "Sinh", "lastName" -> "Le", "age" -> 18))

    MappableHelper.materialize[StudentClazz](Map("firstName" -> Some("Sinh"), "lastName" -> "Le", "age" -> Some(18)))

    newStudent.firstName should be(Some("Sinh"))
  }


  "hasValue " should "convertible to case class object" in {
    val student: HasValues = HasValueHelper.createStudent

    val stu = HasValuesMapperHelper.materialize[StudentClazz](student, Student)

    stu.firstName should be(Some("Sinh"))
  }
}
