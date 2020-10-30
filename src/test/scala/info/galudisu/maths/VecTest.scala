package info.galudisu.maths

import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.{Matchers, PropSpec}

class VecTest extends PropSpec with Matchers {
  val genPositiveDouble: Gen[Double] = Gen.chooseNum(0.0, Double.MaxValue)

  implicit val genVec: Gen[Vec] = for {
    x <- genPositiveDouble
    y <- genPositiveDouble
  } yield Vec(x, y)

  implicit val arbVec: Arbitrary[Vec] = Arbitrary(genVec)

  property("Full rotation should be back where you started") {
    forAll { (v: Vec) =>
      (v rotate Angle.Full).angle == v.angle
    }
  }
}
