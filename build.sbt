ThisBuild / scalaVersion := "2.13.10"

ThisBuild / version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "play-scala-pac4j-sample",
    scalafmtOnCompile := true,
    PlayKeys.playDefaultPort := 9001
  )

libraryDependencies ++= Seq(
  guice,
  "com.google.inject" % "guice" % "6.0.0",
  "com.google.inject.extensions" % "guice-assistedinject" % "6.0.0"
)

libraryDependencies ++= Seq(
  "org.pac4j" %% "play-pac4j" % "11.1.0-PLAY2.8",
  "org.pac4j" % "pac4j-http" % "5.3.0",
  "org.apache.shiro" % "shiro-core" % "1.11.0"
)


libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "2.1.214",
  "org.flywaydb" %% "flyway-play" % "7.38.0",
  "com.typesafe.play" %% "play-slick" % "5.1.0"
)

dependencyOverrides ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-annotations",
  "com.fasterxml.jackson.core" % "jackson-core",
  "com.fasterxml.jackson.core" % "jackson-databind",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-cbor",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-toml",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310",
  "com.fasterxml.jackson.module" % "jackson-module-parameter-names",
  "com.fasterxml.jackson.module" % "jackson-module-paranamer",
  "com.fasterxml.jackson.module" %% "jackson-module-scala"
).map(_ % "2.15.0")

libraryDependencies += "org.springframework.security" % "spring-security-crypto" % "6.0.3"
