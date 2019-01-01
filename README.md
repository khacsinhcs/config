# CONFIGURATION LANGUAGE AGAIN?

## Why?
Many projects configure by Json, Annotation or Xml. But all these data kind is not code
Then we can't handle many hard case in there.

Scala is a extendable language. It should be the best suitable for configuration language.

What we want to config:
1. Domain object
2. Resource
3. Data Transfer Object

As a Developer, we don't want to have many dummy code. Code should be compact and clean. If we keep 
write dummy code, then we tent to be dummy dev :D

Let's see. What will we do????

Example, we want config domain object like:
```aidl
  
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

```

## An opportunity to generate code

There are many duplicate code in concept level. Let think about dto class (`case class`) and
Data Access Layer class (example table config in [Slick](http://slick.lightbend.com/doc/3.0.0/gettingstarted.html)). Why we have to tell that an Student entity has
firstName 2 times, one in `case class`, and another one in `Table Config`

Suppose that you want to build [GraphQL application](https://github.com/sangria-graphql/sangria), then you have to config graphQL configuration again.

All of these thing are duplicate in idea level. We have to write these configuration code again and again. It 
kill our productivity. Why don't we write global config language (we can named it domain language) and then generate all 
another configuration?
