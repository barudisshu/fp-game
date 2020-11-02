import java.awt.Dimension

import javax.swing.plaf.nimbus.NimbusLookAndFeel
import javax.swing.{JFrame, UIManager}

import scala.swing.Swing.ActionListener
import scala.swing._
import scala.swing.event.ButtonClicked

object Main extends SimpleSwingApplication {
  UIManager.setLookAndFeel(new NimbusLookAndFeel)
  JFrame.setDefaultLookAndFeelDecorated(true)

  lazy val top: Frame = new MainFrame {
    contents = new BorderPanel {

      val buttonPanel: FlowPanel = new FlowPanel {
        val truceButton: RadioButton = new RadioButton("停战") { selected = true }
        val easyButton               = new RadioButton("简单")
        val hardButton               = new RadioButton("困难")
        val restartButton            = new Button("重新开始")
        val exitButton               = new Button("退出")
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
    iconImage = toolkit.getImage("images/favicon.png")
    size = new Dimension(1200, 800)
    centerOnScreen()
    visible = true
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
