package com.alab.generate

import com.alab.conf.TypeSystem

class HtmlGenerator extends Generator {
  override def generate: String = {
    val generator = html.list_domain_object
    val htmlResult = generator.apply(TypeSystem.types.values)
    htmlResult.toString()
  }

  override def name: String = "domain_module"
}
