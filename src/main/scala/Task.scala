class Task {
  def greeting(name: String) = s"Welcome, $name"
}


import org.scalatest.funsuite.AnyFunSuite

class TaskTest extends AnyFunSuite {
  test("Greet should return string that contains name") {
    assert(
      (new Task).greeting("kek").contains("kek")
    )
  }
}
