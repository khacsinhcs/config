package com.alab

import com.alab.conf.Type
import com.alab.implicits.HasValuesMapper
import com.alab.model.HasValues

package object syntax {

  implicit class MaterializeOps[A <: HasValues](value: A) {
    def materialize[M](kind: Type)(implicit mapper: HasValuesMapper[M]): M = mapper.map(value, kind)
  }

}
