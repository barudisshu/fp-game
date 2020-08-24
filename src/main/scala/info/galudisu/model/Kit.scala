package info.galudisu.model

/**
 * 道具
 */
sealed trait Kit

/**
 * 药包：回复生命
 */
sealed trait HealthKit extends Kit

/**
 * 升级包：各种不同的升级包
 */
sealed trait UpgradeKit extends Kit

/**
 * 武器包：切换不同的武器
 */
sealed class WeaponKit extends Kit