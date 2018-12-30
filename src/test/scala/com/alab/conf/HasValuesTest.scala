package com.alab.conf

import com.alab.Student
import com.alab.model.MapValues
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.HashMap

class HasValuesTest extends FlatSpec with Matchers {
  it should "get value from Map" in {
    def student = new MapValues(HashMap(
      "first_name" -> "Sinh",
      "age" -> "18"
    ))

    import com.alab.Student._
    student ~> first_name should be(Some("Sinh"))
    student ~> last_name should be(None)
    student ~> age should be(Some(18))
    student demand first_name should be("Sinh")

    the [IllegalStateException] thrownBy {
      student demand last_name
    } should have message "Field(last_name, Last name) is demand"
  }

  it should "to string has value" in {
    def student = new MapValues(HashMap(
      "first_name" -> "Sinh",
      "age" -> "18"
    ))

    val str = student toString Student
    println(str)
    str should include ("Student")
  }
}