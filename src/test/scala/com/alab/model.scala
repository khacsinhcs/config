package com.alab.model

import com.alab.conf._

package object App {

  trait HasName extends HasType {
    val first_name: Field[String] = f("first_name", "First name", StringType)
    val last_name: Field[String] = f("last_name", "Last name", StringType)
    val full_name: Field[String] = func("full_name", "Full name", StringType, hasValues => {
      if (!(hasValues contains first_name) && !(hasValues contains last_name)) {
        None
      }
      Some((hasValues.get(first_name, "") + " " + hasValues.get(last_name, "")).trim)
    })
  }

  trait HasPhone extends HasType {
    val phone: Field[String] = f("phone", "Phone number", required = false, PhoneType)
  }

  trait HasId extends HasType {
    val id: Field[Int] = f("id", "Id", required = false, IdKey)
  }

  trait HasKeyName extends HasType {
    val key_name: Field[String] = f("key_name", "Key Name", required = true, StringKey)
  }

  trait HasContactInfo extends HasName with HasPhone

  object Student extends Type(n = "Student", des = "Student") with HasId with HasContactInfo {
    val age: Field[Int] = f("age", IntType)
    val teacher: FK[String] = fk("teacher", StringKey, Teacher)
    val subjects: Field[List[String]] = f("subjects", StringType.listType)
  }

  object Faculty extends Type(n = "Faculty", des = "The teaching staff of a university or college, or of one of its departments or divisions, viewed as a body.") with HasId {
    val name: Field[String] = f("name", "Name", StringType)
  }

  object Teacher extends Type(n = "Teacher", des = "He teach student") with HasKeyName with HasContactInfo {
    val faculty: FK[Int] = fk("faculty", "faculty", required = true, IdKey, Faculty)
  }

  object Bootstrap {
    def allTypes(): Unit = {
      Faculty
      Teacher
      Student
    }
  }

}


