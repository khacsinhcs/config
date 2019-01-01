package com.alab.conf

import com.alab.model._
import org.scalatest.{FlatSpec, Matchers}

class CaseClassTest extends FlatSpec with Matchers {
  Bootstrap allTypes()

  def generate(t: Type): String = {
    def fieldNameToVariable(name: String): String = {
      val scalaName = name.split("_").map(s => s.charAt(0).toUpper + s.substring(1)).mkString("")
      scalaName.charAt(0).toLower + scalaName.substring(1)
    }

    def params(fields: Iterable[Field[_]]): String = {
      fields.map(f => {
        fieldNameToVariable(f.name) + ": " + (f.required match {
          case true => f.dataType.typeToString
          case false => "Option[" + f.dataType.typeToString + "]"
        })
      }).mkString(", ")
    }


    "case class " + t.name + "(" + params(t.fields) + ")"
  }

  it should "generate case class from Type" in {
    val student = Student
    val caseStudent = generate(student)
    val allClasses = TypeSystem.types.values.map(t => generate(t)).mkString("\n")
    println(allClasses)
    caseStudent should startWith ("case class Student(")
  }
}
