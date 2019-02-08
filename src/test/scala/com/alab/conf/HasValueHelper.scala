package com.alab.conf

import com.alab.model.{HasValues, MapValues}

import scala.collection.immutable.HashMap

object HasValueHelper {
  def createStudent: HasValues = {
    MapValues(HashMap(
      "first_name" -> "Sinh",
      "age" -> "18",
      "teacher" -> HashMap("first_name" -> "Oanh")
    ))
  }
}
