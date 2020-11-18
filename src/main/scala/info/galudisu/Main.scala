import info.galudisu.stage.{StartingState, TankGame, World}
import info.galudisu.ui.PaintWorld
import io.reactivex.rxjavafx.observables.JavaFxObservable
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.{Canvas, GraphicsContext}
import javafx.scene.image.Image
import javafx.scene.layout.StackPane
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

    val root = new StackPane(canvas)
    root.setAlignment(javafx.geometry.Pos.BOTTOM_CENTER)
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
