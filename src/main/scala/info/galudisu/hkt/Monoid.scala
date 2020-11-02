package info.galudisu.hkt

/**
  * 群
  * @tparam G group
  */
trait Monoid[G] {
  def op(a: G, b: G): G
  def zero: G
}
