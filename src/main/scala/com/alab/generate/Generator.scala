package com.alab.generate

import java.io.File
import java.io.PrintWriter

import com.alab.conf.{Field, Type, TypeSystem}

abstract class Generator(config: Config) {
  def generate: String

  def name: String

  def write: Unit = {
    val writer = new PrintWriter(new File(config.path + "/" + name))
    writer.write(generate)
    writer.close()
  }
}

object CaseClassGenerator extends Generator(Config("../scala-2.12/src_managed")) {
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
        normalizeName(f.name) + ": " + (f.required match {
          case true => f.dataType.typeToString
          case false => "Option[" + f.dataType.typeToString + "]"
        })
      }).mkString(", ")
    }


    "case class " + t.name + "(" + params(t.fields) + ")"
  }

  override def name: String = "module"
}