package info.galudisu.hkt

import scala.annotation.tailrec

object Free {

  implicit val f0Functor: Functor[Function0] = new Functor[Function0] {
    override def map[A, B](a: () => A)(f: A => B): () => B = () => f(a())
  }

  final case class Return[F[_], A](a: A)                                     extends Free[F, A]
  final case class Suspend[F[_], A](s: F[A])                                 extends Free[F, A]
  final case class FlatMapped[F[_], A, B](a: Free[F, A], f: A => Free[F, B]) extends Free[F, B]

  def point[F[_], A](value: A): Free[F, A]                             = Return[F, A](value)
  def liftF[F[_], A](value: F[A]): Free[F, A]                          = Suspend(value)
  def void[F[_], A](fa: Free[F, Unit]): Free[F, Unit]                  = fa.map(_ => ())
  def bind[F[_], A, B](fa: Free[F, A])(f: A => Free[F, B]): Free[F, B] = fa.flatMap(f)
}

sealed trait Free[F[_], A] {
  self =>
  import Free._
  def unit(a: A): Free[F, A]                     = Return(a)
  def map[B](f: A => B): Free[F, B]              = flatMap(f andThen (Return(_)))
  def flatMap[B](f: A => Free[F, B]): Free[F, B] = FlatMapped(this, f)

  def >>[B](f: => Free[F, B]): Free[F, B] = bind(this)(_ => f)

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
