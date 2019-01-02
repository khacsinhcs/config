package com.alab.generate

import java.io.File
import java.io.PrintWriter

import com.alab.conf._

abstract class Generator(config: Config) {
  def generate: String

  def name: String

  def write: Unit = {
    val file = new File(config.path + "/" + name)
    if (!file.exists) file.createNewFile
    val writer = new PrintWriter(file)
    writer.write(generate)
    writer.close()
  }
}

object CaseClassGenerator extends Generator(Config("/Users/khacsinhcs/workplace/self/config/target/scala-2.12/src_managed")) {
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
        val `type` = f.dataType match {
          case StringType => "String"
          case PhoneType => "String"
          case StringKey => "String"
          case NumberType => "Double"
          case IntType => "Int"
          case IdKey => "Int"
        }
        val dataType = if (f.required) `type` else "Option[" + `type` + "]"
        normalizeName(f.name) + ": " + dataType
      }).mkString(", ")
    }


    "case class " + t.name + "(" + params(t.fields) + ")"
  }

  override def name: String = "module/App.scala"
}