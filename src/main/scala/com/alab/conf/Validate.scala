package com.alab.conf

object validate {

  trait Validate[T]

  case class ValidateSuccess[T]() extends Validate[T]()

  case class ValidateFail[T](reason: T) extends Validate[T]()


  abstract class Validator[T, K](t: T) extends (T => Validate[K])
}
