import org.scalatest.funsuite.AsyncFunSuite

import java.util.concurrent.atomic.AtomicReference
import scala.collection.mutable.HashMap
import scala.util.Random
import scala.collection.concurrent.TrieMap

class Shortener {
  val shortToFull = Map.empty[String, String]
  val fullToShort = Map.empty[String, String]
  case class State(shortToFull: TrieMap[String, String], fullToShort: TrieMap[String, String])
  val state = new AtomicReference(State(TrieMap.empty, TrieMap.empty))

  state.compareAndExchange()
  def getShortLink(fullLink: String): String =
    if (fullToShort.contains(fullLink))
      fullToShort(fullLink)
    else {
      val shortLink = Random.alphanumeric.take(5).mkString
      fullToShort.(fullLink, shortLink)
      shortToFull.put(shortLink, fullLink)
      shortLink
    }
  def getFullLink(shortLink: String): Option[String] = shortToFull.get(shortLink)
}


import org.scalatest.funsuite.AsyncFunSuite


class ShortenerTest extends AsyncFunSuite {
  test("Greet should return string that contains name") {
    val example = "https://example.com"
    val shortener = new Shortener()
    assert(
      shortener.getFullLink(shortener.getShortLink(example)).contains(example)
    )
  }
}
