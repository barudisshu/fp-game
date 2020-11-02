package info.galudisu.model

import info.galudisu.ai.Moves
import info.galudisu.ai.Moves._
import info.galudisu.maths._

object Tank {
  // 加速
  val Acceleration = 3.0
  // 摩擦
  val Friction: Percentage = Percentage(0.4)
  // 射程
  val GunRange = 10000.0
  // 子弹速度
  val MissileSpeed = 10.0
  // 速度
  val MaxSpeed = 50
  // 转向率
  val RotationRate: Angle = Angle.degrees(5)
  // 大小
  val Size: Dim = Dim(10, 10)

  def apply(entityId: String, pos: Vec): Tank = {
    val physics = Physics(Angle.Zero, pos, Vec.Zero, Vec.Zero, Tank.Size, Friction, MaxSpeed)
    new Tank(EntityId(entityId), AIDone, physics, Angle.Zero, true)
  }

  /**
    * 解构具体哪种类型的tank
    */
  def unapply(e: Entity): Option[Tank] = e match {
    case tank: Tank => Some(tank)
    case _          => None
  }

}

/**
  * 坦克: 有各种各样的坦克
  */
case class Tank(id: EntityId, ai: AI[Unit], physics: Physics, gunAngle: Angle, alive: Boolean) extends Entity {
  type This = Tank
  def replaceId(newId: EntityId): Tank           = copy(id = newId)
  def gunFacing: Angle                           = physics.facing + gunAngle
  def accelerate: Tank                           = accelerateForward(Tank.Acceleration)
  def withAI(newAI: Moves.AI[Unit]): Tank        = copy(ai = newAI)
  def kill: Tank                                 = copy(alive = false, ai = AIDone)
  def fire: Missile                              = Missile.fireToward(pos + Vec.fromAngle(facing, 20.0), facing, Tank.MissileSpeed, Tank.GunRange)
  def updatePhysics(f: Physics => Physics): Tank = copy(physics = f(physics))
}
