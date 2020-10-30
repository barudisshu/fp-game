package info.galudisu.common

import com.typesafe.scalalogging.LazyLogging
import org.specs2.ScalaCheck
import org.specs2.matcher.ShouldMatchers
import org.specs2.mutable.Specification

import scala.annotation.tailrec

class MonadSpecs extends Specification with ShouldMatchers with ScalaCheck with LazyLogging {

  "the monoid" should {
    implicit val intPlusMonoid: Monoid[Int] = new Monoid[Int] {
      override def op(a: Int, b: Int): Int = a + b
      override def zero: Int               = 0
    }

    "available " in prop { (a: Int, b: Int) =>
      intPlusMonoid.op(a, b) should_== (a + b)
      intPlusMonoid.zero should_== 0
    }

    def sum[A](ts: List[A])(implicit m: Monoid[A]): A = ts./:(m.zero)(m.op)

    "random int list" in prop { list: List[Int] =>
      sum(list) must_== list.sum
    }
  }

  "the functor" should {
    def listFunctor = new Functor[List] {
      override def map[A, B](a: List[A])(f: A => B): List[B] = a map f
    }

    "mapping into string" in prop { list: List[Int] =>
      listFunctor.map(list)(x => x.toString) should containTheSameElementsAs(list.map(_.toString))
    }
  }

  "the endofunctor" should {
    def listEndofunctor = new Endofunctor[List] {
      override def map[A](a: List[A])(f: A => A): List[A] = a map f
    }

    "always mapping into self" in prop { list: List[Int] =>
      listEndofunctor.map(list)(_ + 1) should containTheSameElementsAs(list.map(_ + 1))
    }
  }

  // monad是用来描述一种ADT的数据类型
  "the monad" should {
    val listMonad = new Monad[List] {
      override def flatMap[A, B](value: List[A])(func: A => List[B]): List[B] = {
        @tailrec
        def fun(a: List[A], b: List[B]): List[B] = a match {
          case Nil     => b
          case x :: xs => fun(xs, b ::: func(x))
        }

        fun(value, Nil)
      }
      override def unit[A](a: A) = List(a)
    }
    "mapping a high-kinded type" in prop { list: List[Int] =>
      listMonad.flatMap(list)(x => x + 3 :: Nil) should containTheSameElementsAs(list.map(_ + 3))
    }
  }

  "the monad" should {

    sealed trait Tree[+A]
    final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
    final case class Leaf[A](value: A)                        extends Tree[A]

    class TreeMonad extends Monad[Tree] {
      override def flatMap[A, B](v: Tree[A])(f: A => Tree[B]): Tree[B] = v match {
        case Leaf(k)             => f(k)
        case Branch(left, right) => Branch(flatMap(left)(f), flatMap(right)(f))
      }
      override def unit[A](a: A): Tree[A] = Leaf(a)
    }

    "representation of algebraic data type" in {

      val treeMonad: Monad[Tree] = new TreeMonad
      val adts                   = Branch(Branch(Leaf(0), Leaf(1)), Branch(Leaf(2), Leaf(3)))
      val adtsStr                = Branch(Branch(Leaf("0"), Leaf("1")), Branch(Leaf("2"), Leaf("3")))
      val expected               = Branch(Branch(Leaf(1), Leaf(2)), Branch(Leaf(3), Leaf(4)))
      val c: Int => Tree[String] = treeMonad.compose[Int, Int, String](x => Leaf(x), y => Leaf(y.toString))

      val bn: Tree[Tree[Int]] = Leaf(Branch(Leaf(0), Leaf(1)))

      treeMonad.map(adts)(_ + 1) should_=== expected
      treeMonad.flatMap(adts)(x => Leaf(x + 1)) should_=== expected
      treeMonad.flatMap(adts)(c) should_=== adtsStr
      treeMonad.join(bn) should_=== Branch(Leaf(0), Leaf(1))
    }
  }

}
