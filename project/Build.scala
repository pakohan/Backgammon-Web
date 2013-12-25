import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "Backgammon-Web"
  val appVersion = "1.0-SNAPSHOT"

  resolvers += "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
  resolvers += "MVNREPOSITORY" at "http://repo1.maven.org/maven2"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "com.google.inject" % "guice" % "3.0",
    "org.ektorp" % "org.ektorp" % "1.3.0",
    "org.hibernate" % "hibernate" % "3.5.4-Final",
    "org.hibernate" % "hibernate-core" % "3.5.4-Final",
    "org.hibernate" % "hibernate-annotations" % "3.5.6-Final",
    //"mysql" % "mysql-connector-java" % "5.1.27",
    //"org.slf4j" % "slf4j-api" % "1.7.5",
    //"org.slf4j" % "slf4j-simple" % "1.7.5",
    "javassist" % "javassist" % "3.12.1.GA"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}
