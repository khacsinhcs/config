object Student extends BaseType(name = "Student", description = "Student")(new TypeSystem()) {
  val first_name = f("first_name", "First name", StringType)
  val last_name = f("first_name", "Last name", StringType)
}
