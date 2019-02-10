package com.alab.conf

import com.alab.model.App._
import com.alab.model.{HasValues, HasValuesMapper, HasValuesMapperHelper, MapValues}
import com.alab.{Mappable, MappableHelper}
import org.scalatest.{FlatSpec, Matchers}

class CaseClassHandlerSpec extends FlatSpec with Matchers {

  Bootstrap allTypes()

  import Mappable._

  val stu = StudentClazz(Some("Sinh"), 10)

  case class StudentClazz(firstName: Option[String],
                          age: Int)

  "student" should "mapify" in {
    val map = MappableHelper.mapify(stu)
    map should contain key "firstName"
    val student = MapValues(map)
    student(Student.first_name)

    val newStudent = MappableHelper.materialize[StudentClazz](Map("firstName" -> "Sinh", "lastName" -> "Le", "age" -> 18))

    val otherStudent = MappableHelper.materialize[StudentClazz](Map("firstName" -> Some("Sinh"), "lastName" -> "Le", "age" -> Some(18)))

    newStudent.firstName should be(Some("Sinh"))
    newStudent should be(otherStudent)
  }

  class StudentMapper extends HasValuesMapper[StudentClazz] {
    override def map(hasValues: HasValues, kind: Type): StudentClazz = {
      StudentClazz({
        kind ? "first_name" match {
          case Some(field) => hasValues(field)
          case None => None
        }
      }.asInstanceOf[Option[String]], {
        kind ? "age" match {
          case Some(field) => hasValues.demand(field)
        }
      }.asInstanceOf[Int])

    }
  }


  "hasValue " should "convertible to case class object" in {
    val student: HasValues = HasValueHelper.createStudent
    val stu = HasValuesMapperHelper.materialize[StudentClazz](student, Student)
    stu.firstName should be(Some("Sinh"))
  }
}
