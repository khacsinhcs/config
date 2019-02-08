package com.alab.conf

import org.scalatest.{FlatSpec, Matchers}

class CaseClassHandlerSpec extends FlatSpec with Matchers {

  case class Student(firstName: Option[String],
                     lastName: Option[String],
                     age: Option[Int])

}
