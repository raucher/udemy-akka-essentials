package playground

import scala.collection.mutable
import scala.util.Random

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


  def naiveProdCons = {
    val container = new SimpleContainer()

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

  def smartProdCons = {
    val container = new SimpleContainer()

    val consumer = new Thread(() => {
      println("[consumer] starts...")
      container.synchronized {
        if (container.isEmpty) {
          println("[consumer] passively waiting...")
          container.wait()
          println("[consumer] woke up...")
        }
        println(s"[consumer] has consumed ${container.get()}")
      }
    })

    val producer = new Thread(() => {
      println("[producer] starts...")
      container.synchronized {
        if (container.isEmpty) {
          println("[producer] is computing...")
          val computedVal = Random.nextInt()
          println(s"[producer] has computed $computedVal...")
          container.set(computedVal)
          println("[producer] notifying all waiting threads...")
          container.notifyAll()
        } else {
          println("[producer] value is not consumed, waiting...")
          container.wait()
        }
      }
    })

    consumer.start()
    producer.start()
    consumer.join()
    producer.join()
  }

  // Prod-Cons Level 2
  class SimpleBuffer {
    private val MAX_CAPACITY = 3;
    private var buffer: List[Int] = Nil

    def isFull: Boolean = buffer.size >= MAX_CAPACITY

    def isEmpty: Boolean = buffer.isEmpty

    def add(n: Int): Unit = buffer = buffer :+ n

    def get(): Int = {
      val head = buffer.head
      buffer = buffer.tail
      head
    }
  }


  def multipleProdCons(): Unit = {
    val buffer = new SimpleBuffer

    val producer = new Thread(() => {
      println("[producer] started")
      buffer.synchronized {
        while (true) {
          while (!buffer.isFull) {
            println("[producer] buffer is not full, generating...")
            val newVal = Random.nextInt()
            println(s"[producer] generated $newVal")
            buffer.add(newVal)
            println("[producer] notifying all waiting threads")
            buffer.notifyAll()
          }
          println("[producer] buffer is full, waiting...")
          buffer.wait()
        }
      }
    })

    class Consumer(private val buffer: SimpleBuffer, name: String) extends Thread {
      override def run(): Unit = {
        println(s"[$name] started")
        buffer.synchronized {
          while (true) {
            if (buffer.isEmpty) {
              println(s"[$name] notifies all waiting threads")
              buffer.notifyAll()
              println(s"[$name] buffer is empty, waiting...")
              buffer.wait()
            }
            println(s"[$name] consuming value")
            println(s"[$name] consumed ${buffer.get()}")

          }
        }
      }
    }

    //    val consumer1 = new Thread(() => {
    //      println("[consumer] started")
    //      buffer.synchronized {
    //        while (true) {
    //          if (buffer.isEmpty) {
    //            println("[consumer] notifies all waiting threads")
    //            buffer.notifyAll()
    //            println("[consumer] buffer is empty, waiting...")
    //            buffer.wait()
    //          } else {
    //            println("[consumer] consuming value")
    //            println(s"[consumer] consumed ${buffer.get()}")
    //          }
    //        }
    //      }
    //    })

    val consumer1 = new Consumer(buffer, "consumer-1")
    val consumer2 = new Consumer(buffer, "consumer-2")
    val consumer3 = new Consumer(buffer, "consumer-3")

    consumer1.start()
    consumer2.start()
    consumer3.start()
    producer.start()
    consumer1.join()
    consumer2.join()
    consumer3.join()
    producer.join()
  }

  def queueBasedProdCons(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue()
    val MAX_CAPACITY: Int = 3

    val producer = new Thread(() => {
      buffer.synchronized {
        while (true) {
          while (buffer.size == MAX_CAPACITY) {
            println(s"[producer] buffer is full, waiting...")
            buffer.wait()
          }
          println(s"[producer] generating...")
          val generatedValue = Random.nextInt()
          println(s"[producer] generated $generatedValue")
          buffer.enqueue(generatedValue)
          println(s"[producer] notifying waiting threads")
          buffer.notifyAll()
        }
      }
    })

    val consumer = new Thread(() => {
      buffer.synchronized {
        while (true) {
          while (buffer.isEmpty) {
            println(s"[consumer] buffer is empty, waiting...")
            buffer.wait()
          }
          val consumedValue = buffer.dequeue()
          println(s"[consumer] consumed $consumedValue")
          println(s"[consumer] notifying waiting threads")
          buffer.notifyAll()
        }
      }
    })

    producer.start()
    consumer.start()
    producer.join()
    consumer.join()
  }

  queueBasedProdCons()
}
