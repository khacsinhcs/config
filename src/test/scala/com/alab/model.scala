package com.alab

import com.alab.conf._

package object model {

  trait HasName extends HasType {
    val first_name: Field[String] = f("first_name", "First name", StringType)
    val last_name: Field[String] = f("last_name", "Last name", StringType)
  }

  trait HasPhone extends HasType {
    val phone: Field[String] = f("phone", "Phone number", required = false, PhoneType)
  }

  trait HasId extends HasType {
    val id: Field[Int] = f("id", "Id", required = true, IdKey)
  }

  trait HasKeyName extends HasType {
    val key_name: Field[String] = f("key_name", "Key Name", required = true, StringKey)
  }

  trait HasContactInfo extends HasName with HasPhone

  object Student extends Type(name = "Student", description = "Student") with HasId with HasContactInfo {
    val age: Field[Int] = f("age", IntType)
    val teacher: FK[String] = fk("teacher", StringKey, Teacher)
  }

  object Teacher extends Type(name = "Teacher", description = "He teach student") with HasKeyName with HasContactInfo

  object Bootstrap {
    def allTypes(): Unit = {
      Teacher
      Student
    }
  }

}


