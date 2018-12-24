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
class Student extents Type {
	name : "Student",
	description: "This is an example of student configuration"
	fields: {
		first_name = Field(name: "first_name", label: "First Name", type: StringType),
		last_name = Field(name: "last_name", label: "Last Name", type: StringType),
		email = Field(name: "email", label: "Email", type: EmailType)
	}
}

```

