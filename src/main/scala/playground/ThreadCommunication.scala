package playground

object ThreadCommunication extends App {
  class Container {
    private var value: Int = 0

    /**
      * sets new value. Has no side-effects
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


  }

}
