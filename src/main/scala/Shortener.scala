import org.scalatest.funsuite.AsyncFunSuite

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random


class Shortener {
  val shortToFull = mutable.HashMap.empty[String, String]
  val fullToShort = mutable.HashMap.empty[String, String]

  def getShortLink(fullLink: String): String = synchronized {
    if (fullToShort.contains(fullLink))
      fullToShort(fullLink)
    else {
      val shortLink = Random.alphanumeric.take(5).mkString
      fullToShort.put(fullLink, shortLink)
      shortToFull.put(shortLink, fullLink)
      shortLink
    }
  }

  def getFullLink(shortLink: String): Option[String] = shortToFull.get(shortLink)
}


import org.scalatest.funsuite.AsyncFunSuite

class ShortenerTest extends AsyncFunSuite {
  implicit override def executionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  val possibleLinks = (1 to 1000).map("https://example.com/link_" + _)

  test("Full link of a created short link should be equals to original link") {
    val example = "https://example.com"
    val shortener = new Shortener()
    assert(
      shortener.getFullLink(shortener.getShortLink(example)).contains(example)
    )
  }

  test("Short link should be created only once") {
    val shortener = new Shortener

    val futuresList = (1 to 10000).map {
      _ =>
      Future {
        val processedLinks = possibleLinks
          .map(shortener.getShortLink)
          .flatMap(shortener.getFullLink(_).toList)
        possibleLinks == processedLinks
      }
    }

    Future
      .sequence(futuresList)
      .map(checks => assert(!checks.contains(false)))

  }

}
