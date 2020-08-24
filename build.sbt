organization in ThisBuild := "info.galudisu"

name := """fp-game"""

maintainer := "Galudisu <galudisu@gmail.com>"

/* scala versions and options */
scalaVersion in ThisBuild := "2.12.8"

// dependencies versions
lazy val log4jVersion        = "2.7"
lazy val scalaLoggingVersion = "3.7.2"
lazy val chillVersion        = "0.9.5"
lazy val slf4jVersion        = "1.7.25"
lazy val simulacrumVersion   = "0.13.0"
lazy val catsVersion         = "2.1.1"
lazy val scalaSwingVersion   = "2.1.1"
lazy val doodleVersion       = "0.9.21"

// make version compatible with docker for publishing
ThisBuild / dynverSeparator := "-"

// This work for jdk >= 8u131
javacOptions in Universal := Seq(
  "-J-XX:+UnlockExperimentalVMOptions",
  "-J-XX:+UseCGroupMemoryLimitForHeap",
  "-J-XX:MaxRAMFraction=1",
  "-J-XshowSettings:vm"
)

// These options will be used for *all* versions.
scalacOptions in ThisBuild ++= Seq(
  "-unchecked",
  "-feature",
  "-language:_",
  "-Ypartial-unification",
  "-Xfatal-warnings",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)
resolvers ++= Seq(
  "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

mainClass in (Compile, run) := Some("info.galudisu.Main")

libraryDependencies ++= {
  Seq(
    "org.apache.logging.log4j"   % "log4j-core"       % log4jVersion,
    "org.apache.logging.log4j"   % "log4j-api"        % log4jVersion,
    "org.apache.logging.log4j"   % "log4j-slf4j-impl" % log4jVersion,
    "com.typesafe.scala-logging" %% "scala-logging"   % scalaLoggingVersion,
    "com.twitter"                %% "chill-akka"      % chillVersion,
    "org.typelevel"              %% "cats-core"       % catsVersion,
    "org.typelevel"              %% "cats-kernel"     % catsVersion,
    "org.typelevel"              %% "cats-macros"     % catsVersion,
    "org.scala-lang.modules"     %% "scala-swing"     % scalaSwingVersion,
    "org.creativescala"          %% "doodle"          % doodleVersion
  )
}

addCompilerPlugin("org.spire-math"  %% "kind-projector" % "0.9.7")
addCompilerPlugin("org.scalamacros" % "paradise"        % "2.1.1" cross CrossVersion.full)
