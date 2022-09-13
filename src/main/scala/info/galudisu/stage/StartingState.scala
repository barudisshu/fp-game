package info.galudisu.stage

import info.galudisu.ai.{Moves, TruceTankAI}
import info.galudisu.maths.{Dim, Vec}
import info.galudisu.model.Tank

object StartingState {
  import Moves.*

  def fireAndRush: AI[Unit] =
    for {
      t <- findNearestTank
      _ <- aimAtTank(t)
      _ <- fire
      _ <- accelerate * 4
    } yield ()

  val tanks = List(
    Tank("1", Vec(10, 10)) withAI loop(fireAndRush),
    Tank("2", Vec(200, 200)) withAI loop(
      for {
        t <- findNearestTank
        _ <- aimAwayFrom(t)
        _ <- accelerate * 20
      } yield ()),
    Tank("3", Vec(500, 400)) withAI loop(
      for {
        _ <- moveTo(Vec(200, 200))
        _ <- fire
        _ <- moveTo(Vec(400, 200))
        _ <- fire
        _ <- moveTo(Vec(400, 400))
        _ <- fire
        _ <- moveTo(Vec(200, 400))
        _ <- fire
      } yield ()),
    Tank("4", Vec(250, 400)) withAI loop(
      for {
        _ <- rotateLeft
        _ <- fire
        _ <- accelerate * 4
      } yield ()),
  )

  val world: World   = World(Dim(1000, 700), tanks)
  val game: TankGame = TankGame(world, TruceTankAI, 0)
}
