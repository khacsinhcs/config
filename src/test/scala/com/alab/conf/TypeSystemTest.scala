package com.alab.conf

import org.scalatest.{FlatSpec, Matchers}
import com.alab.model.App.{Bootstrap, Teacher}

class TypeSystemTest extends FlatSpec with Matchers {
  Bootstrap allTypes()

  "Teacher" should "get front type system" in {
    TypeSystem ? "Teacher" should be(Some(Teacher))
  }

  "First name field" should "get front type" in {
    Teacher ? "first_name" should be(Some(Teacher.first_name))
  }

}
