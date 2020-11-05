package info.galudisu.hkt

trait Monoid[G] {
  def op(a: G, b: G): G
  def zero: G
}
