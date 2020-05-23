import ch.maximelovino.inquirer4s.{Confirm, Prompt}

object Main extends App {
  val stringList = (0 to 100).map(index => {
    val stringRepresentation = (0 to index).foldLeft("")((string: String, shift) => string + ('A' + shift).toChar)
    s"$index - $stringRepresentation"
  })
  //stringList.foreach(println)

  val answers = Prompt(Seq(Confirm("Are you sure?"), Confirm("Do you want to delete this?"))).prompt()
  println(answers)
}
