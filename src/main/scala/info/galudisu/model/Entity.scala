package info.galudisu.model

import info.galudisu.ai.Moves.AI
import info.galudisu.maths.{Angle, Rect, Vec}

object EntityId {
  val Auto: EntityId = EntityId("[AUTO]")
}

case class EntityId(name: String) extends AnyVal with Ordered[EntityId] {
  override def compare(that: EntityId): Int = name compare that.name
}

trait Alive {
  type This <: Alive

  def kill: This
  def alive: Boolean
  final def onlyIfAlive[A](a: A)(f: A => A): A = if (alive) f(a) else a
  final def dead: Boolean = !alive
}

trait Physical {
  type This <: Physical

  def physics: Physics
  def updatePhysics(f: Physics => Physics): This

  def run(allowMove: Vec => Boolean): This = updatePhysics(_ run allowMove)

  final def pos: Vec      = physics.pos
  final def vel: Vec      = physics.vel
  final def acc: Vec      = physics.acc
  final def facing: Angle = physics.facing
  final def bounds: Rect  = physics.bounds

  final def accelerateForward(acc: Double): This                 = updatePhysics(_ accelerateForward acc)
  final def distanceTo(other: Physical): Double                  = pos distanceTo other.pos
  final def rotateBy(a: Angle, upTo: Option[Angle] = None): This = updatePhysics(_.rotateBy(a, upTo))
  final def rotateTo(a: Angle): This                             = updatePhysics(_ rotateTo a)
}

trait AIControlled {
  type This <: AIControlled
  def ai: AI[Unit]
  def withAI(newAI: AI[Unit]): This
}

trait Entity extends Physical with AIControlled with Alive {
  type This <: Entity

  def id: EntityId
  def replaceId(newId: EntityId): This
  final def sameAs(other: Entity): Boolean = other.id == id
}
