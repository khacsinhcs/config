package com.alab.conf

import com.alab.Mappable
import com.alab.model.MapValues
import org.scalatest.{FlatSpec, Matchers}

class CaseClassHandlerSpec extends FlatSpec with Matchers {

  import Mappable._
  case class Student(firstName: Option[String],
                     lastName: Option[String],
                     age: Option[Int])

  val stu = Student(Some("Sinh"), Some("le"), Some(10))

  def mapify[T: Mappable](t: T) = implicitly[Mappable[T]].toMap(t)

  def materialize[T: Mappable](map: Map[String, Any]) = implicitly[Mappable[T]].fromMap(map)


  "student" should "mapify" in {
    val map = mapify(stu)
    map should contain key "firstName"
    val student = MapValues(map)
  }
}
