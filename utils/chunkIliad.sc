import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source
import java.io.PrintWriter
import java.io.File

val f = "iliad.cex"
val index = "paragraph-index.csv"
val repo = TextRepositorySource.fromCexFile(f)
val iliad = repo.corpus ~~ CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")

val lines = Source.fromFile(index).getLines.toVector

for (l <- lines.tail) {
  def cols = l.split(",")
  if (cols.size > 1) {
    val urn = CtsUrn(cols(1))
    val docName = "Iliad-" + urn.passageComponent + ".txt"
    val psg =   iliad ~~ urn
    val contents = psg.nodes.map(_.text).mkString("\n")
    new PrintWriter(new File(docName)){write(contents);close}
    println("Wrote " + contents + "to " + docName)
  }
}

//
//psg1.nodes.map(_.text).mkString("\n")
