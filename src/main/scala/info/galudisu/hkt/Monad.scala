package info.galudisu.hkt

import scala.language.higherKinds

/**
  * monad
  * @tparam F 函子
  */
trait Monad[F[_]] {
  def flatMap[A, B](v: F[A])(f: A => F[B]): F[B]
  def unit[A](a: A): F[A]

  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] = a => flatMap(f(a))(g)
  def map[A, B](ma: F[A])(f: A => B): F[B]                    = flatMap(ma)(a => unit(f(a)))
  def join[A](mma: F[F[A]]): F[A]                             = flatMap(mma)(ma => ma)
}
