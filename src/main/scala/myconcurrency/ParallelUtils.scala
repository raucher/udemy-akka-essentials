package myconcurrency

import scala.collection.parallel.immutable.ParVector

class ParallelUtils extends App {
  val parallelList = List(1, 2, 3).par // .par converts to ParSeq[A]
  val parallelVector1 = Vector(1, 2, 3).par
  val parallelVector2 = ParVector(1, 2, 3) // the same as above


}
