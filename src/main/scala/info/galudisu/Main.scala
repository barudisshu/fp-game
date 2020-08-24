package info.galudisu

// The "Image" DSL is the easiest way to create images
import doodle.image._
// Colors and other useful stuff
import doodle.core._

// Extension methods
import doodle.image.syntax._
// Render to a window using Java2D (must be running in the JVM)
import doodle.java2d._

object Main extends App {

  val blackSquare = Image.rectangle(30, 30).fillColor(Color.black)
  val redSquare = Image.rectangle(30, 30).fillColor(Color.red)

  // A chessboard, broken into steps showing the recursive construction
  val twoByTwo =
    (redSquare.beside(blackSquare))
      .above(blackSquare.beside(redSquare))

  val fourByFour =
    (twoByTwo.beside(twoByTwo))
      .above(twoByTwo.beside(twoByTwo))

  val chessboard =
    (fourByFour.beside(fourByFour))
      .above(fourByFour.beside(fourByFour))

  chessboard.draw()

}
