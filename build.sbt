organization in ThisBuild := "info.galudisu"

name := """fp-game"""

maintainer := "Galudisu <galudisu@gmail.com>"

/* scala versions and options */
scalaVersion in ThisBuild := "2.12.8"

// dependencies versions
lazy val log4jVersion        = "2.7"
lazy val scalaLoggingVersion = "3.7.2"
lazy val rxVersion           = "0.27.0"
lazy val rxjavafxVersion     = "2.2.2"
lazy val slf4jVersion        = "1.7.25"
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
    // reactivex
    "io.reactivex" %% "rxscala" % rxVersion,
    // ui design
    "io.reactivex.rxjava2"   % "rxjavafx"     % rxjavafxVersion,
    // test
    "org.specs2"     %% "specs2-core"       % specsVersion      % Test,
    "org.specs2"     %% "specs2-mock"       % specsVersion      % Test,
    "org.specs2"     %% "specs2-scalacheck" % specsVersion      % Test,
    "org.scalatest"  %% "scalatest"         % scalatestVersion  % Test,
    "org.scalacheck" %% "scalacheck"        % scalacheckVersion % Test
  )
}
