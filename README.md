# CONFIGURATION LANGUAGE AGAIN?

Many project start configuration by Json, Annotation or Xml. But all these data kind is not code
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
  trait HasName extends com.alab.type.HasType {
    val first_name = f("first_name", "First name", com.alab.type.StringType)
    val last_name = f("last_name", "Last name", com.alab.type.StringType)
  }

  trait HasPhone extends com.alab.type.HasType {
    val phone = f("phone", "Phone number", required = false, com.alab.type.PhoneType)
  }

  trait HasContactInfo extends HasName with HasPhone

  object Student extends com.alab.type.Type(name = "Student", description = "Student") with HasContactInfo {
    val age = f("age", com.alab.type.StringType)
  }

  object Teacher extends com.alab.type.Type(name = "Teacher", description = "He teach student") with HasContactInfo

```

