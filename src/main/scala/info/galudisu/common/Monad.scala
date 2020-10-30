package info.galudisu.common

import scala.annotation.tailrec
import scala.language.higherKinds

/**
  * 群
  * @tparam G group
  */
trait Monoid[G] {
  def op(a: G, b: G): G
  def zero: G
}

/**
  * 函子
  * @tparam F functor
  */
trait Functor[F[_]] {
  def map[A, B](a: F[A])(f: A => B): F[B]
}

/**
  * 自函子
  * @tparam F 函子
  */
trait Endofunctor[F[_]] {
  def map[A](a: F[A])(f: A => A): F[A]
}

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

object Free {

  implicit val f0Functor: Functor[Function0] = new Functor[Function0] {
    override def map[A, B](a: () => A)(f: A => B): () => B = () => f(a())
  }

  final case class Return[F[_], A](a: A)                                     extends Free[F, A]
  final case class Suspend[F[_], A](s: F[A])                                 extends Free[F, A]
  final case class FlatMapped[F[_], A, B](a: Free[F, A], f: A => Free[F, B]) extends Free[F, B]

  def point[F[_], A](value: A): Free[F, A]    = Return[F, A](value)
  def liftF[F[_], A](value: F[A]): Free[F, A] = Suspend(value)
}

/**
  * CoYoneda则是，将协变（a -> b）变成逆变 (b -> a)
  * (b -> a) -> F b ≅ F a
  *
  * FreeMonad的形式总是先“挂起”，然后再“消费”
  *
  * Free Monad是一种Trampoline模式的泛化
  */
sealed trait Free[F[_], A] {
  import Free._
  def unit(a: A): Free[F, A]                     = Return(a)
  def map[B](f: A => B): Free[F, B]              = flatMap(f andThen (Return(_)))
  def flatMap[B](f: A => Free[F, B]): Free[F, B] = FlatMapped(this, f)

  /** flatMap 别名 */
  final def >>=[B](f: A => Free[F, B]): Free[F, B]                               = this flatMap f
  final def fold[B](r: A => B, s: F[Free[F, A]] => B)(implicit F: Functor[F]): B = resume.fold(s, r)

  final def go(f: F[Free[F, A]] => Free[F, A])(implicit F: Functor[F]): A = {
    @tailrec def loop(t: Free[F, A]): A = t.resume match {
      case Left(s)  => loop(f(s))
      case Right(r) => r
    }
    loop(this)
  }

  @tailrec
  final def resume(implicit F: Functor[F]): Either[F[Free[F, A]], A] = this match {
    case Return(a)  => Right(a)
    case Suspend(s) => Left(F.map(s)(Return(_)))
    case FlatMapped(sub, f) =>
      sub match {
        case Return(a)        => f(a).resume
        case Suspend(s)       => Left(F.map(s)(f))
        case FlatMapped(d, g) => d.flatMap(dd => g(dd).flatMap(f)).resume
      }
  }
}
