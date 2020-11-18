package info.galudisu.ui

import info.galudisu.maths._
import info.galudisu.model._
import info.galudisu.stage.World
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

trait PaintWorld {

  def world: World
  def paintBackground(width: Double, height: Double)(implicit gc: GraphicsContext): Unit = {
    gc.setFill(Color.LIGHTGRAY)
    val worldW = world.bounds.width
    val worldH = world.bounds.height
    gc.translate(width / 2 - worldW / 2, height / 2 - worldH / 2)
    paintWorld
  }

  def paintWorld(implicit gc: GraphicsContext): Unit = {
    gc.setFill(Color.WHITE)
    fillRect(world.bounds)
    gc.setFill(Color.BLACK)
    paintRect(world.bounds)
    world.entities foreach paintEntity
  }

  def paintRect(r: Rect)(implicit gc: GraphicsContext) {
    val (x, y, w, h) = r.toSizeIntTuple
    gc.strokeRect(x, y, w, h)
  }

  def fillRect(r: Rect)(implicit gc: GraphicsContext) {
    val (x, y, w, h) = r.toSizeIntTuple
    gc.fillRect(x, y, w, h)
  }

  def paintEntity(e: Entity)(implicit gc: GraphicsContext): Unit = e match {
    case t: Tank    => paintTank(t)
    case m: Missile => paintMissile(m)
    case x          => sys.error("Unexpected entity: " + x)
  }

  def paintTank(t: Tank)(implicit gc: GraphicsContext): Unit = {
    gc.setFill(if (t.dead) Color.GRAY else Color.BLACK)
    fillRect(t.bounds)
    val (x1, y1) = t.pos.toIntTuple
    val vec      = Vec.fromAngle(t.facing, 20.0)
    val (x2, y2) = (t.pos + vec).toIntTuple
    gc.strokeLine(x1, y1, x2, y2)
  }

  def paintMissile(m: Missile)(implicit gc: GraphicsContext): Unit = {
    val (x, y, width, height) = m.bounds.toSizeIntTuple
    gc.setFill(Color.RED)
    gc.fillOval(x, y, width, height)
  }
}
