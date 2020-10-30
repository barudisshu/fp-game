package info.galudisu.model

import info.galudisu.maths._

/**
  * 物理作用
  * @param facing 面向
  * @param pos 位置
  * @param vel 下个位置
  * @param acc 加速度
  * @param size 大小
  * @param friction 摩擦力
  * @param maxSpeed 最大速度
  */
case class Physics(facing: Angle, pos: Vec, vel: Vec, acc: Vec, size: Dim, friction: Percentage, maxSpeed: Double) {
  def run(allowMove: Vec => Boolean): Physics = {
    val nextPos = constrainPos(allowMove, pos + vel)
    val nextVel = constrainVel((vel * frictionMult.amount) + acc)
    Physics(facing, nextPos, nextVel, Vec.Zero, size, friction, maxSpeed)
  }

  def nextFrameAllowed(allowMove: Vec => Boolean): Boolean = allowMove(pos + vel)

  lazy val bounds: Rect = size centeredAt pos

  def accelerateForward(acc: Double): Physics = accelerate(Vec.fromAngle(facing, acc))

  def accelerate(acc: Vec): Physics = copy(acc = this.acc + acc)

  def rotateTo(that: Angle): Physics = copy(facing = that)

  def rotateBy(by: Angle, upTo: Option[Angle] = None): Physics =
    copy(facing = upTo.fold(facing + by)(facing.addUpTo(by, _)))

  private def frictionMult = friction.complement

  private def constrainPos(allowMove: Vec => Boolean, newPos: Vec): Vec = if (allowMove(newPos)) newPos else pos
  private def constrainVel(newVel: Vec): Vec                            = if (newVel.length > maxSpeed) vel.withLength(maxSpeed) else newVel
}
