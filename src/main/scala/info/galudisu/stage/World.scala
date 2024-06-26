package info.galudisu.stage

import info.galudisu.ai.MoveInterpreter
import info.galudisu.maths.*
import info.galudisu.model.*

import scala.collection.SortedMap
import scala.collection.immutable.TreeMap

object World {

  def apply(bounds: Dim, entities: Seq[Entity]): World =
    new World(bounds, TreeMap(entities.map(t => t.id -> t)*), 1)
}

class World private (
    dimensions: Dim,
    entityMap: SortedMap[EntityId, Entity],
    nextId: Int) {
  lazy val entities: Seq[Entity] = entityMap.values.toIndexedSeq
  private lazy val entityIds     = entityMap.keys

  val bounds: Rect = Rect(0.0, 0.0, dimensions.width, dimensions.height)

  def find(id: EntityId): Option[Entity] = entityMap.get(id)

  def nearestTankTo(e: Entity): Option[Tank] = {
    val otherTanks = (entityMap - e.id).values.collect {
      case Tank(t) if t.alive => t
    }
    if otherTanks.nonEmpty then {
      Some(otherTanks.minBy(_ distanceTo e))
    }
    else {
      None
    }
  }

  def collidingEntities(rect: Rect): Seq[Entity] =
    entities.filter(_.bounds intersects rect)

  private def sanitizeId(e: Entity): Entity =
    if e.id == EntityId.Auto then e.replaceId(EntityId(s"[AUTO-$nextId]"))
    else e

  def updateEntity(id: EntityId)(f: Entity => Entity): World = {
    find(id).fold(this) { e =>
      new World(dimensions, entityMap.updated(id, f(e)), nextId)
    }
  }

  def withEntity(e: Entity): World = {
    val e2 = sanitizeId(e)
    new World(dimensions, entityMap + (e2.id -> e2), nextId + 1)
  }

  def runFrame(interpreter: MoveInterpreter): World =
    runPhysics.killCollidingEntities.runAI(interpreter)

  private def killCollidingEntities: World = {
    val newlyKilled = for {
      e <- entities
      x <- collidingEntities(e.bounds) if !e.sameAs(x)
    } yield x.id -> x.kill

    new World(dimensions, entityMap ++ newlyKilled, nextId)
  }

  private def runPhysics: World = new World(dimensions, entityMap mapValues (_ run bounds.containsPt), nextId)

  private def removeDead: World = new World(dimensions, removeEntitiesIf(_.dead), nextId)

  private def runAI(interpreter: MoveInterpreter): World = (this /: entities)(interpreter)

  private def removeEntitiesIf(p: Entity => Boolean) = entityMap.filterNot(kv => p(kv._2))
}
