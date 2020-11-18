package info.galudisu.hkt

/**
  * 函子
  * @tparam F functor
  */
trait Functor[F[_]] {
  def map[A, B](a: F[A])(f: A => B): F[B]
}
