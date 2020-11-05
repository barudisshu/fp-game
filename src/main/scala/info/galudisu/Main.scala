import java.awt.Dimension

import info.galudisu.ai._
import info.galudisu.stage.{StartingState, TankGame, World}
import info.galudisu.ui.PaintWorld
import javax.swing.plaf.nimbus.NimbusLookAndFeel
import javax.swing.{JFrame, UIManager}

import scala.swing.Swing.ActionListener
import scala.swing._
import scala.swing.event.ButtonClicked

object Main extends SimpleSwingApplication {
  UIManager.setLookAndFeel(new NimbusLookAndFeel)
  JFrame.setDefaultLookAndFeelDecorated(true)

  var game: TankGame = StartingState.game

  lazy val top: Frame = new MainFrame {
    contents = new BorderPanel {

      val tankPanel: Panel with PaintWorld = new Panel with PaintWorld {
        def world: World = game.world
      }

      val buttonPanel: FlowPanel = new FlowPanel {
        val truceButton: RadioButton = new RadioButton("停战") { selected = true }
        val easyButton: RadioButton  = new RadioButton("简单")
        val hardButton: RadioButton  = new RadioButton("困难")
        val restartButton            = new Button("重新开始")
        val exitButton               = new Button("退出")
        contents += (truceButton, easyButton, hardButton, restartButton, exitButton)

        val buttonGroup = new ButtonGroup(truceButton, easyButton, hardButton)
        listenTo(truceButton, easyButton, hardButton, restartButton, exitButton)

        reactions += {
          case ButtonClicked(`truceButton`) => game = game withInterpreter TruceTankAI
          case ButtonClicked(`easyButton`)  => game = game withInterpreter EasyTankAI
          case ButtonClicked(`hardButton`)  => game = game withInterpreter HardTankAI
          case ButtonClicked(`restartButton`) =>
            game = StartingState.game
            truceButton.selected = true

          case ButtonClicked(`exitButton`) => sys.exit()
        }
      }

      add(buttonPanel, BorderPanel.Position.North)
      add(tankPanel, BorderPanel.Position.Center)
    }
    title = "AI坦克大战"
    iconImage = toolkit.getImage("images/favicon.ico")
    size = new Dimension(1200, 800)
    centerOnScreen()
    visible = true
  }

  val gameTimer = new javax.swing.Timer(40, ActionListener { _ =>
    game = game.runFrame
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
