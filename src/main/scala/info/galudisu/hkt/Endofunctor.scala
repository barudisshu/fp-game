package info.galudisu.hkt

trait Endofunctor[F[_]] {
  def map[A](a: F[A])(f: A => A): F[A]
}
