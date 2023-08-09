package playground

object MultithreadingRecap extends App {
  println("MultithreadingRecap")

  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("From runnable")
  })

  aThread.start()
  aThread.join()

  val range = 1.to(10, 2)
  range.foreach(println)

  val threadHello = new Thread(() => (1 to 100).foreach { x =>
//    Thread.sleep(100)
    println("hello!!!")
  })

  val threadGoodbye = new Thread(() => (1 to 100).foreach { x =>
//    Thread.sleep(100)
    println("goodbye...")
  })

  threadHello.start()
  threadGoodbye.start()

  threadHello.join()
  threadGoodbye.join()
}
