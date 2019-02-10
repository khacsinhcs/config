package com.alab.conf

import com.alab.Mappable
import com.alab.model.App._
import com.alab.model.MapValues
import org.scalatest.{FlatSpec, Matchers}

class CaseClassHandlerSpec extends FlatSpec with Matchers {

  Bootstrap allTypes()

  import Mappable._

  val stu = Student(Some("Sinh"), Some("le"), 10)

  case class Student(firstName: Option[String],
                     lastName: Option[String],
                     age: Int)

  def mapify[T: Mappable](t: T) = implicitly[Mappable[T]].toMap(t)

  def materialize[T: Mappable](map: Map[String, Any]) = implicitly[Mappable[T]].fromMap(map)


  "student" should "mapify" in {
    val map = mapify(stu)
    map should contain key "firstName"
    val student = MapValues(map)
    val myMap: Map[String, Any] = Map("firstName" -> "Sinh", "lastName" -> "Le", "age" -> 18)

    val newStudent = materialize[Student](Map("firstName" -> "Sinh", "lastName" -> "Le", "age" -> 18))

    materialize[Student](Map("firstName" -> Some("Sinh"), "lastName" -> "Le", "age" -> Some(18)))

    newStudent.firstName should be(Some("Sinh"))
  }
}
