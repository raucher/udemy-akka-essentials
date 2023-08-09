package playground

object ThreadCommunication extends App {
  class SimpleContainer {
    private var value: Int = 0

    /**
      * Sets new value. Has no side-effects
      *
      * @param newValue
      */
    def set(newValue: Int) = value = newValue

    /**
      * Returns the value and resets container
      *
      * @return
      */
    def get(): Int = {
      val temp = value
      value = 0
      temp
    }

    def isEmpty: Boolean = value == 0
  }

  val container = new SimpleContainer()

  def naiveProdCons = {
    val consumer = new Thread(() => {
      println("[consumer] starts...")

      while (container.isEmpty) {
        println("[consumer] actively waiting...")
      }

      println(s"[consumer] consumes ${container.get()}")
    })

    val producer = new Thread(() => {
      println("[producer] starts...")
      if (container.isEmpty) {
        println("[producer] computing...")
        val newVal = 42
        println(s"[producer] has computed $newVal")
        container.set(42)
      } else {
        println("[producer] waiting value to be consumed...")
      }
    })

    consumer.start()
    producer.start()
    consumer.join()
    producer.join()
  }

//  naiveProdCons
}
