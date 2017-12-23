name := """play-java-forms-example"""

version := "2.6.x"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.2"

testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

libraryDependencies += guice

// Resolver is needed only for SNAPSHOT versions
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"


libraryDependencies ++= Seq(
  "com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B3",
  "com.google.guava" % "guava" % "12.0",
  "com.google.code.gson" % "gson" % "2.8.0"
)
