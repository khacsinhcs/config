/**
  * Scala macro to convert between a case class instance and a Map of constructor parameters.
  * Developed by Jonathan Chow (see http://blog.echo.sh/post/65955606729/exploring-scala-macros-map-to-case-class-conversion for description and usage).
  * This version simply updates Jonathan's code to Scala 2.11.2
  */
package com.alab

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

trait Mappable[T] {
  def toMap(t: T): Map[String, Any]

  def fromMap(map: Map[String, Any]): T
}

object Mappable {

  implicit def materializeMappable[T]: Mappable[T] = macro materializeMappableImpl[T]

  def materializeMappableImpl[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[Mappable[T]] = {
    println("Start debug")
    import c.universe._
    val tpe = weakTypeOf[T]
    val companion = tpe.typeSymbol.companion
    println(companion)

    val fields = tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get.paramLists.head


    val (toMapParams, fromMapParams) = fields.map { field =>
      val name = field.asTerm.name
      val key = name.decodedName.toString
      val returnType = tpe.decl(name).typeSignature
      println(returnType)

      (q"$key -> t.$name", q"map($key).asInstanceOf[$returnType]")
    }.unzip


    c.Expr[Mappable[T]] {
      q"""
      new Mappable[$tpe] {
        def toMap(t: $tpe): Map[String, Any] = Map(..$toMapParams)
        def fromMap(map: Map[String, Any]): $tpe = $companion(..$fromMapParams)
      }
    """
    }
  }
}