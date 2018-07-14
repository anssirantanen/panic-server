package mocModels

import models.ProducerModel

object ProducerMocs {
  val mockWithoutId = ProducerModel(None,"name1", "desc",List())
  val mockWithId = ProducerModel(Some("id"),"name1", "desc",List())
}
