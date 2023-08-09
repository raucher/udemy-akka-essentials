package myconcurrency

object Intro extends App {
  println("Hello!")

  //  val pool = Executors.newFixedThreadPool(5)
  //  pool.execute(() => {
  //    Thread.sleep(1000)
  //    println(s"${Thread.currentThread().getName}: done after 1 second")
  //  })
  //
  //  pool.execute(() => {
  //    Thread.sleep(1000)
  //    println(s"${Thread.currentThread().getName}: not done after 1 second")
  //    Thread.sleep(1000)
  //    println(s"${Thread.currentThread().getName}: done after 2 seconds")
  //  })
  //
  //  pool.shutdown()

  // all reads and writes of @volatile are automatically synchronized
  class BankAccount( // @volatile
                     var amount: Int) {
    override def toString: String = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    //    println(s"I've bought $thing")
    //    println(s"My account is now $account")
  }

  def synchronizedBuy(account: BankAccount, thing: String, price: Int) = {
    account.synchronized {
      account.amount -= price
    }
    //    println(s"I've bought $thing")
    //    println(s"My account is now $account")

  }

  for (_ <- 1 to 1000) {
    val account = new BankAccount(50000)
    val thread1 = new Thread(() => synchronizedBuy(account, "shoes", 3000))
    val thread2 = new Thread(() => synchronizedBuy(account, "iPhone12", 5000))

    thread1.start()
    thread2.start()
    Thread.sleep(10)

    if (account.amount != 42000) {
      println(s"SYNC: AHA, amount is ${account.amount}")
    }
    //    println()
  }

  for (_ <- 1 to 1000) {
    val account = new BankAccount(50000)
    val thread1 = new Thread(() => buy(account, "shoes", 3000))
    val thread2 = new Thread(() => buy(account, "iPhone12", 5000))

    thread1.start()
    thread2.start()
    Thread.sleep(10)

    if (account.amount != 42000) {
      println(s"NOT-SYNC: AHA, amount is ${account.amount}")
    }
    //    println()
  }
}
