package info.galudisu.maths

import scala.annotation.tailrec
import scala.math.*

object Rect {
  def apply(
      x1: Double,
      y1: Double,
      x2: Double,
      y2: Double): Rect =
    new Rect(min(x1, x2), min(y1, y2), max(x1, x2), max(y1, y2))
  def unapply(rect: Rect): Option[(Double, Double, Double, Double)] =
    Some(rect.x1, rect.y1, rect.x2, rect.y2)
}

/**
 * 矩阵
 */
final class Rect private (
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double) {
  def width: Double     = x2 - x1
  def height: Double    = y2 - y1
  def bounds: Dim       = Dim(width, height)
  def size: Dim         = Dim(width, height)
  def isEmpty: Boolean  = width == 0 || height == 0
  def nonEmpty: Boolean = !isEmpty

  def topLeft: Vec      = Vec(x1, y1)
  def topRight: Vec     = Vec(x2, y1)
  def bottomRight: Vec  = Vec(x2, y2)
  def bottomLeft: Vec   = Vec(x1, y2)
  def center: Vec       = Vec(x1 + width / 2, y1 + height / 2)
  def corners: Seq[Vec] = List(topLeft, topRight, bottomRight, bottomLeft)

  def toTuple: (Double, Double, Double, Double)     = (x1, y1, x2, y2)
  def toIntTuple: (Int, Int, Int, Int)              = (x1.round.toInt, y1.round.toInt, x2.round.toInt, y2.round.toInt)
  def toSizeTuple: (Double, Double, Double, Double) = (x1, y1, width, height)
  def toSizeIntTuple: (Int, Int, Int, Int) = (x1.round.toInt, y1.round.toInt, width.round.toInt, height.round.toInt)

  def containsX(x: Double): Boolean = x >= x1 && x < x2
  def containsY(y: Double): Boolean = y >= y1 && y < y2
  def containsPt(pt: Vec): Boolean  = containsX(pt.x) && containsY(pt.y)
  def containsRect(rect: Rect): Boolean =
    (containsX(rect.x1) && containsX(rect.x2)
    && containsY(rect.y1) && containsY(rect.y2))

  def intersects(other: Rect): Boolean = {
    val Rect(a1, b1, a2, b2) = other
    nonEmpty && other.nonEmpty &&
    x1 < a2 && x2 > a1 &&
    y1 < b2 && y2 > b1
  }

  def cropX(x: Double): Double =
    if x >= x2 then x2 - 1
    else if x < x1 then x1
    else x

  def cropY(y: Double): Double =
    if y >= y2 then y2 - 1
    else if y < y1 then y1
    else y

  def cropPt(pt: Vec): Vec = Vec(cropX(pt.x), cropY(pt.y))

  @tailrec
  private def wrap(
      c: Double,
      lim: Double,
      c1: Double): Double =
    if c < 0 then wrap(c + lim, lim, c1)
    else if c > lim then wrap(c - lim, lim, c1)
    else c % lim + c1

  def wrapX(x: Double): Double = wrap(x, width, x1)
  def wrapY(y: Double): Double = wrap(y, height, y1)
  def wrapPt(pt: Vec): Vec     = Vec(wrapX(pt.x), wrapY(pt.y))

  def centreInRect(outerRect: Rect): Rect = {
    val center = outerRect.center
    val newX1  = center.x - width / 2
    val newY1  = center.y - height / 2
    Rect(newX1, newY1, newX1 + width, newY1 + height)
  }

  private def equality = (x1, y1, x2, y2)

  override def toString = s"Rect($x1, $y1, $x2, $y2)"

  override def hashCode(): Int = equality.hashCode

  override def equals(a: Any): Boolean = a match {
    case r: Rect => r.equality == equality
    case _       => false
  }
}
