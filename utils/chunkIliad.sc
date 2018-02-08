import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source
import java.io.PrintWriter
import java.io.File

val f = "iliads.cex"
val index = "paragraph-index.csv"
val wheng = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.wheng:")

def paragraphReferences(indexFile: String) = {//: Vector[CtsUrn] = {
  val lines = Source.fromFile(indexFile).getLines.toVector
  // read second column with URN from file.
  // omit null entries for replacement pages


  val refOptions = for (l <- lines.tail) yield {
    def cols = l.split(",")
    if (cols.size > 1) {
      val urn = CtsUrn(cols(1))
      Some(urn)
    } else {
      None
    }
  }
  refOptions.flatten

}

def writeOutput(iliad: Corpus, urns: Vector[CtsUrn], fileBase: String): Unit = {
  for (urn <- urns.tail.take(10)) {
    val psg =   iliad ~~ urn
    val contents = psg.nodes.map(_.text).mkString("\n")
    val fileName = fileBase + "-" + urn.passageComponent + ".txt"
    new PrintWriter(new File(fileName)){write(contents);close}
    println("Wrote " + contents + "to " + fileName)
  }
}



def chunkVersion(cexFile: String, indexFile: String, iliadUrn: CtsUrn): Unit = {
  val repo = TextRepositorySource.fromCexFile(cexFile)
  val iliad = repo.corpus ~~ iliadUrn
  val lines = Source.fromFile(index).getLines.toVector
  val urns = paragraphReferences(indexFile)
  writeOutput(iliad, urns, "iliad-" + iliadUrn.version)
}
