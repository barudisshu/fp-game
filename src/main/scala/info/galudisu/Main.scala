import info.galudisu.stage.{StartingState, TankGame, World}
import info.galudisu.ui.PaintWorld
import io.reactivex.rxjavafx.observables.JavaFxObservable
import javafx.application.Application
import javafx.geometry.{Insets, Pos}
import javafx.scene.Scene
import javafx.scene.canvas.{Canvas, GraphicsContext}
import javafx.scene.control.{Button, RadioButton}
import javafx.scene.image.Image
import javafx.scene.layout.{BorderPane, FlowPane, HBox, VBox}
import javafx.stage.Stage
import javafx.util.Duration

import scala.language.postfixOps

class TankWorld extends Application {

  val width  = 1200
  val height = 800

  var game: TankGame = StartingState.game
  val world: PaintWorld = new PaintWorld {
    override def world: World = game.world
  }

  override def start(primaryStage: Stage): Unit = {

    val canvas                       = new Canvas(width, height)
    implicit val gc: GraphicsContext = canvas.getGraphicsContext2D

    val buttonPanel = new HBox()

    val content = new FlowPane()

    val truceButton   = new RadioButton("停战")
    val easyButton    = new RadioButton("简单")
    val hardButton    = new RadioButton("困难")
    val restartButton = new Button("重新开始")
    val exitButton    = new Button("退出")

    content.setPadding(new Insets(10))
    content.setHgap(10)
    content.setAlignment(Pos.CENTER)
    content.getChildren.addAll(truceButton, easyButton, hardButton, restartButton, exitButton)
    buttonPanel.getChildren.add(content)
    buttonPanel.setAlignment(Pos.CENTER)

    val tankPanel = new HBox(canvas)
    tankPanel.setAlignment(Pos.CENTER)

    val root = new BorderPane()
    root.setTop(buttonPanel)
    root.setCenter(tankPanel)

    world.paintBackground(width, height)

    JavaFxObservable
      .interval(Duration.millis(100))
      .doOnNext { _ =>
        world.paintWorld
      }
      .subscribe()

    primaryStage.setScene(new Scene(root, width, height))
    primaryStage.setTitle("Tank game")
    primaryStage.getIcons.add(new Image("images/favicon.ico"))
    primaryStage.show()
  }

}

object Main extends App {
  Application.launch(classOf[TankWorld])
}
