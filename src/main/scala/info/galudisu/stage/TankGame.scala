package info.galudisu.stage

import info.galudisu.ai.MoveInterpreter

case class TankGame(world: World, interpreter: MoveInterpreter, frame: Int) {
  def withInterpreter(newInterp: MoveInterpreter): TankGame = copy(interpreter = newInterp)
  def runFrame: TankGame                                    = TankGame(world.runFrame(interpreter), interpreter, frame + 1)
}
