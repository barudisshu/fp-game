package info.galudisu.maths

/**
 * 角度
 */
object Angle {

  val Pi: Double      = math.Pi
  val TwoPi: Double   = 2 * Pi
  val PiOver2: Double = Pi / 2

  val Zero: Angle          = Angle(0)
  val Quarter: Angle       = Angle(Pi / 2)
  val Half: Angle          = Angle(Pi)
  val ThreeQuarters: Angle = Angle(3 * Pi / 2)
  val Full: Angle          = Angle(TwoPi)

  extension (degs: Double)
    def degrees: Angle = Angle(degs * Pi / 180.0)
    def radians: Angle = Angle(degs)

  def normalize(radians: Double): Double = {
    val fullCycles       = (radians / TwoPi).asInstanceOf[Int]
    val possiblyNegative = radians - TwoPi * fullCycles

    if possiblyNegative < 0 then possiblyNegative + TwoPi
    else possiblyNegative
  }

  def apply(radians: Double): Angle = new Angle(normalize(radians))
}

/**
 * 角度
 * @param radians
 *   度数
 */
class Angle private (val radians: Double) extends AnyVal with Ordered[Angle] {
  import Angle.*

  def sin: Double = math.sin(radians)
  def cos: Double = math.cos(radians)
  def tan: Double = math.tan(radians)
  // 反向
  def opposite: Angle = Angle(radians + Pi)
  // 度数
  def degrees: Double          = radians * 180.0 / Pi
  def unary_- : Angle          = Angle(-radians)
  def +(other: Angle): Angle   = Angle(radians + other.radians)
  def -(other: Angle): Angle   = Angle(radians - other.radians)
  def *(factor: Double): Angle = Angle(radians * factor)
  def /(factor: Double): Angle = Angle(radians / factor)

  override def compare(that: Angle): Int = {
    if this == that then 0
    else if this.radians < that.radians then -1
    else +1
  }

  private def shiftSin(x: Double)     = math.sin(x - radians - Pi)
  def isLeftOf(that: Angle): Boolean  = (that == opposite) || (that != this && shiftSin(that.radians) < 0)
  def isRightOf(that: Angle): Boolean = (that == opposite) || (that != this && shiftSin(that.radians) > 0)
  def distanceTo(that: Angle): Angle = {
    val diff = that - this
    if diff < Angle.Half then diff else -diff
  }
  def addUpTo(add: Angle, upTo: Angle): Angle = {
    val added = this + add
    if isLeftOf(upTo) != added.isLeftOf(upTo) then upTo else added
  }
  override def toString: String = s"Angle($radians)"
}
