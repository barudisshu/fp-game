package info.galudisu.ai

import info.galudisu.common._
import Free._

import info.galudisu.maths._
import info.galudisu.model.{Entity, Tank}

sealed trait Move[+A]
case class Accelerate[A](next: A) extends Move[A]
case class RotateLeft[A](upTo: Option[Angle], next: A) extends Move[A]
case class RotateRight[A](upTo: Option[Angle], next: A) extends Move[A]
case class Delay[A](next: A) extends Move[A]
case class Fire[A](next: A) extends Move[A]
case class FindNearestTank[A](onFoundTank: Tank => A) extends Move[A]
case class AngleTo[A](toPos: Vec, onAngle: Angle => A) extends Move[A]
case class IsAt[A](pos: Vec, onAt: Boolean => A) extends Move[A]
case class IsFacing[A](angle: Angle, onFacing: Boolean => A) extends Move[A]
case class Me[A](onMe: Entity => A) extends Move[A]

object Move {
  implicit def functor: Functor[Move] = new Functor[Move] {
    override def map[A, B](move: Move[A])(f: A => B): Move[B] = move match {
      case Accelerate(next) => Accelerate(f(next))
      case RotateLeft(upTo, next) => RotateLeft(upTo, f(next))
      case RotateRight(upTo, next) => RotateRight(upTo, f(next))
      case Delay(next) => Delay(f(next))
      case Fire(next) => Fire(f(next))
      case FindNearestTank(onFoundTank) => FindNearestTank(onFoundTank andThen f)
      case AngleTo(toPos, onAngle) => AngleTo(toPos, onAngle andThen f)
      case IsAt(pos, onAt) => IsAt(pos, onAt andThen f)
      case IsFacing(angle, onFacing) => IsFacing(angle, onFacing andThen f)
      case Me(onMe) => Me(onMe andThen f)
    }
  }
}

// Lifting functions.  Note that liftF is implicit within the trait.
trait BasicMoves {
  type AI[A] = Free[Move, A]
  val AIDone: AI[Unit] = point(())
  private implicit def liftMove[A](move: Move[A]): AI[A] = liftF(move)
  def accelerate: AI[Unit] = Accelerate(())
  def rotateLeft: AI[Unit] = RotateLeft(None, ())
  def rotateRight: AI[Unit] = RotateRight(None, ())
  def rotateLeftUpTo(upTo: Angle): AI[Unit] = RotateLeft(Some(upTo), ())
  def rotateRightUpTo(upTo: Angle): AI[Unit] = RotateRight(Some(upTo), ())
  def delay: AI[Unit] = Delay(())
  def fire: AI[Unit] = Fire(())
  def findNearestTank: AI[Tank] = FindNearestTank(identity)
  def angleTo(pos: Vec): AI[Angle] = AngleTo(pos, identity)
  def isAt(pos: Vec): AI[Boolean] = IsAt(pos, identity)
  def isFacing(angle: Angle): AI[Boolean] = IsFacing(angle, identity)
  def me: AI[Entity] = Me(identity)
}

// Fancy composite tank moves
trait AdvancedMoves extends BasicMoves {

}

object Moves extends AdvancedMoves
