package com.alab.conf

import scala.collection.mutabletype

object TypeSystem {
  val types : mutable.Map[String, Type] = mutable.Map[String, Type]()

  def ++(t: Type): TypeSystem.type = {
    types.put(t.name, t)
    this
  }

  def ? (name: String): Option[Type] = {
    types.get(name)
  }
}
