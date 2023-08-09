package playground

import scala.annotation.tailrec

object Exercises extends App {
  // Construct 50 inception threads

  // Variant 1
  def constructThread(end: Int): Thread = {
    @tailrec
    def makeThreads(n: Int, acc: Thread): Thread = {
      if (n >= end) acc
      else {
        val t = new Thread(() => {
          println(s"Hello from thread #$n")
          acc.start()
          acc.join()
        })
        makeThreads(n + 1, t)
      }
    }

    makeThreads(1, new Thread(() => println("Hello from thread #0")))
  }

  //  val iThreads = constructThread(10)
  //  iThreads.start()
  //  iThreads.join()

  // Variant 2
  def inceptionThreads(maxThreads: Int, n: Int = 1): Thread = new Thread(() => {
    if (n < maxThreads) {
      val t = inceptionThreads(maxThreads, n + 1)
      t.start()
      t.join()
    }
    println(s"Hello from thread #$n")
  })

  val iThreads2 = inceptionThreads(10, 1)
  iThreads2.start()
  iThreads2.join()
}
