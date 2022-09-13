package info.galudisu.maths

import scala.math.*

object Vec {
  def fromAngle(angle: Angle, length: Double): Vec = Vec(length * angle.cos, length * angle.sin)
  // 0向量
  val Zero: Vec = Vec(0, 0)
}

/**
 * 向量
 */
case class Vec(x: Double, y: Double) {
  def angle: Angle = {
    if x > 0 then Angle(atan(y / x))
    else if x < 0 then Angle(atan(y / x) + Pi)
    else {
      if y <= 0 then Angle.Zero
      else Angle.Quarter
    }
  }
  def length: Double               = sqrt(x * x + y * y)
  def unary_- : Vec                = Vec(-x, -y)
  def +(vec: Vec): Vec             = Vec(x + vec.x, y + vec.y)
  def -(vec: Vec): Vec             = Vec(x - vec.x, y - vec.y)
  def withLength(len: Double): Vec = Vec.fromAngle(angle, len)
  def *(factor: Double): Vec       = Vec.fromAngle(angle, length * factor)
  def rotate(by: Angle): Vec       = Vec.fromAngle(angle + by, length)
  def /(factor: Double): Vec       = this * (1.0 / factor)
  def toTuple: (Double, Double)    = (x, y)
  def toIntTuple: (Int, Int)       = (x.round.toInt, y.round.toInt)
  def between(vec: Vec): Vec       = vec - this
  def distanceTo(pt: Vec): Double  = between(pt).length
  def angleTo(pt: Vec): Angle      = between(pt).angle
}
