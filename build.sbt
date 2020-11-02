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
lazy val scalaSwingVersion   = "2.1.1"
lazy val specsVersion        = "4.10.5"
lazy val scalatestVersion    = "3.0.4"
lazy val scalacheckVersion   = "1.14.3"

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
    "org.scala-lang.modules"     %% "scala-swing"     % scalaSwingVersion,
    // test
    "org.specs2"     %% "specs2-core"       % specsVersion      % Test,
    "org.specs2"     %% "specs2-mock"       % specsVersion      % Test,
    "org.specs2"     %% "specs2-scalacheck" % specsVersion      % Test,
    "org.scalatest"  %% "scalatest"         % scalatestVersion  % Test,
    "org.scalacheck" %% "scalacheck"        % scalacheckVersion % Test
  )
}
