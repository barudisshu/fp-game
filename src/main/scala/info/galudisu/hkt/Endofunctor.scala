package info.galudisu.hkt

/**
  * 自函子
  * @tparam F 函子
  */
trait Endofunctor[F[_]] {
  def map[A](a: F[A])(f: A => A): F[A]
}
