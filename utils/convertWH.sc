// convert WordHoard XML file to CEX.

val base = "urn:cts:greekLit:tlg0012.tlg001.wheng:"

val f = "wordhoard-english.xml"

import scala.xml.XML

val xml = XML.loadFile(f)

val lines = (xml \\ "p").toVector

val cex = for (l <- lines) yield {
 (l \\ "@id").text.replaceFirst("IL.",base) + "#" + l.text
}
