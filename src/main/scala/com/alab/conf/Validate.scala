package com.alab.conf

object validate {

  abstract class Validate()

  case class ValidateSuccess() extends Validate()

  case class ValidateFail(reason: String) extends Validate()

}
