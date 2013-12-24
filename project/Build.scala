import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "Backgammon-Web"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "com.google.inject" % "guice" % "3.0",
    "org.ektorp" % "org.ektorp" % "1.3.0"
  )

  resolvers += "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}
