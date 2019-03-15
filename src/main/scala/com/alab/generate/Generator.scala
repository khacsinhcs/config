package com.alab.generate

import com.alab.conf._

trait Generator {
  def generate: String

  def name: String
}

object CaseClassGenerator extends Generator {
  override def generate: String = {
    "package model.app" + "\n" + TypeSystem.types.values.map(t => generate(t)).mkString("\n")
  }

  def generate(t: Type): String = {
    def normalizeName(name: String): String = {
      val scalaName = name.split("_").map(s => s.charAt(0).toUpper + s.substring(1)).mkString("")
      scalaName.charAt(0).toLower + scalaName.substring(1)
    }


    def params(fields: Iterable[Field[_]]): String = {
      fields.map(f => {
        def typeToString(dataType: DataType[_]) = dataType match {
          case StringType => "String"
          case PhoneType => "String"
          case StringKey => "String"
          case NumberType => "Double"
          case IntType => "Int"
          case IdKey => "Int"
        }

        val `type` = f.dataType match {
          case ls: ListType[_] => "List[" + typeToString(ls.dataType) + "]"
          case t => typeToString(t)
        }
        val dataType = if (f.required) `type` else "Option[" + `type` + "]"
        normalizeName(f.name) + ": " + dataType
      }).mkString(", ")
    }


    "case class " + t.n + "(" + params(t.fields) + ")"
  }

  override def name: String = "module/App.scala"
}