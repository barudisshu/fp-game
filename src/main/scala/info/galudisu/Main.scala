import java.awt.Dimension

import scala.swing.Swing.ActionListener
import scala.swing.event.ButtonClicked
import scala.swing._

object Main extends SimpleSwingApplication {
  lazy val top: Frame = new MainFrame {
    contents = new BorderPanel {

      val buttonPanel: FlowPanel = new FlowPanel {
        val truceButton: RadioButton = new RadioButton("Truce") { selected = true }
        val easyButton               = new RadioButton("Easy")
        val hardButton               = new RadioButton("Hard")
        val restartButton            = new Button("Restart")
        val exitButton               = new Button("Exit")
        contents += (truceButton, easyButton, hardButton, restartButton, exitButton)

        val buttonGroup = new ButtonGroup(truceButton, easyButton, hardButton)
        listenTo(truceButton, easyButton, hardButton, restartButton, exitButton)

        reactions += {
          case ButtonClicked(`truceButton`)   => sys.exit()
          case ButtonClicked(`easyButton`)    => sys.exit()
          case ButtonClicked(`hardButton`)    => sys.exit()
          case ButtonClicked(`restartButton`) => sys.exit()

          case ButtonClicked(`exitButton`) => sys.exit()
        }
      }

      add(buttonPanel, BorderPanel.Position.North)
    }
    title = "AI坦克大战"
    // centerOnScreen()
    size = new Dimension(800, 600)

  }

  val gameTimer = new javax.swing.Timer(40, ActionListener { _ =>
    top.repaint()
  })

  override def startup(args: Array[String]): Unit = {
    super.startup(args)
    gameTimer.start()
  }

  override def shutdown(): Unit = {
    gameTimer.stop()
    super.shutdown()
  }
}
