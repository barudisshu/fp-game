package info.galudisu.maths

/**
 * 维
 * @param width
 *   宽
 * @param height
 *   高
 */
case class Dim(width: Double, height: Double) {
  def positionedAt(topLeft: Vec): Rect = Rect(topLeft.x, topLeft.y, topLeft.x + width, topLeft.y + height)
  def centeredAt(center: Vec): Rect = {
    val halfW = width / 2
    val halfH = height / 2
    Rect(center.x - halfW, center.y - halfH, center.x + halfW, center.y + halfH)
  }

  def toTuple: (Double, Double) = (width, height)
  def toIntTuple: (Int, Int)    = (width.round.toInt, height.round.toInt)
}
