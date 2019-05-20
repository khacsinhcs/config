package com.alab.syntax

import com.alab.model.HasValues

trait HasValuesWriter[A] {
  def convert(value: A): HasValues
}

