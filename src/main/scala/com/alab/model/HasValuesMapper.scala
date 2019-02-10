package com.alab.model

import com.alab.Mappable

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

trait HasValuesMapper[T] {
  def map(hasValues: HasValues): T
}

object HasValuesMapper {
  implicit def materializeHasValuesMappable[T]: HasValuesMapper[T] = macro materializeHasValuesMappableImpl[T]

  def materializeHasValuesMappableImpl[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[HasValuesMapper[T]] = {
    import c.universe._
    val tpe = weakTypeOf[T]
    val tpeName = tpe.toString
    val companion = tpe.typeSymbol.companion

    val fields = tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get.paramLists.head

    val fromMapParams = fields.map { field =>
      val name = field.asTerm.name
      val key = name.decodedName.toString
      val f = normalizeField(key)
      val returnType = tpe.decl(name).typeSignature
      val isOption = returnType.toString.contains("Option")

      if (isOption)
        q"""{
            (kind ? $f) match {
              case Some(field) => hasValues -> field
              case None => None
            }
         }.asInstanceOf[$returnType]
       """ else
        q"""
            {
              (kind ? $f) match {
                case Some(field) => hasValues.demand(field)
              }
            }.asInstanceOf[$returnType]
          """
    }
    c.Expr[HasValuesMapper[T]] {
      q"""
      new HasValuesMappable[$tpe] {
        def map(hasValues: HasValues): $tpe = {
          (TypeSystem / $tpeName) match {
            case Some(kind) => $companion(..$fromMapParams)
          }
        }
      }
    """
    }
  }

  private def normalizeField(key: String): String = new String(key.toCharArray.flatMap(c => if (c.isUpper) Array('_', c.toLower) else Array(c)))
}

object HasValuesMapperHelper {
  def materialize[T: Mappable](hasValue: HasValues): T = implicitly[HasValuesMapper[T]].map(hasValue)
}
