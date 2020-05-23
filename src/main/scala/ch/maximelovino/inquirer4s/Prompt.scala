package ch.maximelovino.inquirer4s

import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.input.{KeyStroke, KeyType}
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.terminal.{DefaultTerminalFactory, Terminal}

case class Prompt[A](questions: Seq[Question[A]]) {
  def prompt(): Seq[Option[A]] = questions.map(_.run())
}

trait Question[A] {
  val message: String

  def runInScreen(block: Screen => Option[A]): Option[A] = {
    val screen = new DefaultTerminalFactory().createScreen()
    screen.startScreen()
    screen.clear()
    screen.refresh()
    val returnValue = block(screen)
    screen.clear()
    screen.refresh()
    screen.stopScreen(true)
    returnValue
  }

  def run(): Option[A]
}

case class Confirm(override val message: String) extends Question[Boolean] {
  override def run(): Option[Boolean] = {
    runInScreen(screen => {
      val graphics = screen
        .newTextGraphics()
        .setBackgroundColor(TextColor.ANSI.BLACK)
        .setForegroundColor(TextColor.ANSI.WHITE)
      graphics.putString(0, 0, s"$message\ty/n")
      screen.refresh()

      def select(screen: Screen): Boolean = {
        @scala.annotation.tailrec
        def match_input(input: KeyStroke): Boolean = (input.getKeyType, input.getCharacter) match {
          case (KeyType.Character, char) if (char != null && char.charValue() == 'n') => false
          case (KeyType.Character, char) if (char != null && char.charValue() == 'y') => true
          case _ => match_input(screen.readInput())
        }

        match_input(screen.readInput())
      }

      Some(select(screen))

    })
  }
}


