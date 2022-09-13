// dependencies versions
lazy val catsVersion         = "2.8.0"
lazy val log4jVersion        = "2.18.0"
lazy val scalaLoggingVersion = "3.9.5"
lazy val scalaSwingVersion   = "3.0.0"
lazy val specsVersion        = "5.0.7"
lazy val scalatestVersion    = "3.2.12"
lazy val scalacheckVersion   = "3.2.12.0"

// make version compatible with docker for publishing
ThisBuild / scalaVersion := "3.2.0"
ThisBuild / version      := "0.1.0"
ThisBuild / organization := "info.galudisu"
ThisBuild / name         := """fp-game"""
ThisBuild / homepage     := Some(url("https://github.com/barudisshu/fp-game"))
ThisBuild / licenses     := Seq(("CC0", url("https://creativecommons.org/publicdomain/zero/1.0")))
ThisBuild / developers := List(Developer("barudisshu", "Galudisu", "galudisu@gmail.com", url("https://galudisu.info")))

ThisBuild / resolvers ++= Seq(
  "Local Maven Repository".at("file://" + Path.userHome.absolutePath + "/.m2/repository")) ++
  Resolver.sonatypeOssRepos("releases") ++
  Resolver.sonatypeOssRepos("snapshots")

Compile / run / mainClass := Some("info.galudisu.Main")

libraryDependencies ++= {
  Seq(
    "org.apache.logging.log4j"    % "log4j-core"       % log4jVersion,
    "org.apache.logging.log4j"    % "log4j-api"        % log4jVersion,
    "org.apache.logging.log4j"    % "log4j-slf4j-impl" % log4jVersion,
    "com.typesafe.scala-logging" %% "scala-logging"    % scalaLoggingVersion,
    // cats
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-free" % catsVersion,
    "org.typelevel" %% "cats-laws" % catsVersion,
    // ui design
    "org.scala-lang.modules" %% "scala-swing" % scalaSwingVersion,
    // test
    "org.specs2"        %% "specs2-core"       % specsVersion      % Test,
    "org.specs2"        %% "specs2-scalacheck" % specsVersion      % Test,
    "org.scalatest"     %% "scalatest"         % scalatestVersion  % Test,
    "org.scalatestplus" %% "scalacheck-1-16"   % scalacheckVersion % Test,
  )
}
