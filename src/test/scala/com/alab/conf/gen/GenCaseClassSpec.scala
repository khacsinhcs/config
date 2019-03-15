package com.alab.conf.gen

import com.alab.generate.CaseClassGenerator
import com.alab.model.App._
import org.scalatest.{FlatSpec, Matchers}

class GenCaseClassSpec extends FlatSpec with Matchers {
  Bootstrap allTypes()

  it should "generate case class from Type" in {
    CaseClassGenerator.generate(Student)
    val caseStudent = CaseClassGenerator.generate(Student)
    println(caseStudent)
    caseStudent should be("case class Student(id: Option[Int], firstName: String, lastName: String, phone: Option[String], age: Int, teacher: String, subjects: List[String])")
  }

  it should "generate all types" in {
    val caseClasses = CaseClassGenerator.generate
    println(caseClasses)
    caseClasses should include ("Student")
    caseClasses should include ("Teacher")
  }
}
