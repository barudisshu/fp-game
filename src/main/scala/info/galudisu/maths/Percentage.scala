package info.galudisu.maths

object Percentage {

  /** 百分百 */
  val One: Percentage = Percentage(1.0)

  /** 零 */
  val Zero: Percentage = Percentage(0.0)

  def apply(amount: Double): Percentage = new Percentage(normalize(amount))

  def normalize(amount: Double): Double = {
    if (amount < 0) 0
    else if (amount > 1) 1
    else amount
  }
}

/**
  * 百分百
  * @param amount 基数
  */
class Percentage(val amount: Double) extends AnyVal {
  def +(other: Percentage): Percentage = Percentage(amount + other.amount)
  def *(other: Percentage): Percentage = Percentage(amount * other.amount)
  def complement: Percentage           = Percentage(1.0 - amount)
}
