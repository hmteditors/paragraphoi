import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.io.Source
import java.io.PrintWriter
import java.io.File

/** CEX file with text repository. */
val cex = "iliads.cex"
/** Delimited text file with paragraph references in col. 2. */
val index = "paragraph-index.csv"

/** Extract paragraph URNs from index file.
*
* @param indexFile Name of index file.
*/
def paragraphReferences(indexFile: String): Vector[CtsUrn] = {
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


/**
*/
def writeChunked(iliad: Corpus, urns: Vector[CtsUrn], fileBase: String): Unit = {
  for (urn <- urns.tail) {
    val psg =   iliad ~~ urn
    val contents = psg.nodes.map(_.text).mkString("\n")
    val fileName = fileBase + "-" + urn.passageComponent + ".txt"
    new PrintWriter(new File(fileName)){write(contents);close}
    println("Wrote " + contents + "to " + fileName)
  }
}


def pairedCex(urns: Vector[CtsUrn], iliad1: Corpus, iliad2: Corpus):  Vector[String] = {

  val mashup = for (urn <- urns) yield {
    println("\t" + urn + " ...")
    val psg1 =   iliad1 ~~ urn
    val psg2 =   iliad2 ~~ urn

    val contents1 = psg1.nodes.map(_.text).mkString(" ")
    val contents2 = psg2.nodes.map(_.text).mkString(" ")
    val composite = urn.addVersion("gk-eng-mashup")

    composite.toString + "#" + contents1 + " " + contents2
  }
  mashup
}

def mashup() = {
  val repo = TextRepositorySource.fromCexFile(cex)
  println("Collecting corpora...")
  val iliad1 = repo.corpus ~~ CtsUrn("urn:cts:greekLit:tlg0012.tlg001.perseus_grc2:")
  val iliad2 = repo.corpus ~~ CtsUrn("urn:cts:greekLit:tlg0012.tlg001.wheng:")
  println("Collecting indexed URNs...")
  val urns = paragraphReferences(index)
  println("Making new corpus...")
  val cexVector = pairedCex(urns, iliad1, iliad2)
  println("Writing output...")
  new PrintWriter(new File("iliad-mashup.cex")){write(cexVector.mkString("\n") + "\n");close}
}

def chunkVersion(cexFile: String, indexFile: String, iliadUrn: CtsUrn): Unit = {
  val repo = TextRepositorySource.fromCexFile(cexFile)
  val iliad = repo.corpus ~~ iliadUrn
  val lines = Source.fromFile(index).getLines.toVector
  val urns = paragraphReferences(indexFile)
  writeChunked(iliad, urns, "iliad-" + iliadUrn.version)
}

def chunk(iliadUrnString: String): Unit = {
  chunkVersion(cex, index, CtsUrn(iliadUrnString))
}

println("\n\nTo write Iliad files chunked by indexed paragraph:\n")
println("\tchunk(ILIAD_URN)\n")
println("Possible URNs:")
println("\turn:cts:greekLit:tlg0012.tlg001.wheng:")
println("\turn:cts:greekLit:tlg0012.tlg001.perseus_grc2:\n\n")


println("To get a mashup in CEX format:")
println("\tmashup()")
