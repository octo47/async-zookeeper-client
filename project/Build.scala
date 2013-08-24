
import sbt._
import Keys._
import Process._

object AsyncZkClient extends Build {

  val VERSION = "0.2.3"

  val dependencies =
    "com.typesafe.akka"    %% "akka-actor" % "2.2.0" ::
    "org.apache.zookeeper" %  "zookeeper"  % "3.4.3" ::
    "org.scalatest"        %% "scalatest"  % "1.9.1" % "test" ::
    "com.github.bigtoast"  %% "rokprox"    % "0.2.2" % "test" :: Nil

  val publishDocs = TaskKey[Unit]("publish-docs")

  val project = Project(
    id = "async-zk-client",
    base = file("."),
    settings = Defaults.defaultSettings ++ Seq(
      organization := "com.github.bigtoast",
      name         := "async-zk-client",
      version      := VERSION,
      scalaVersion := "2.10.2",

      resolvers ++= Seq(
        "octo47 repo" at "http://octo47.github.io/repo/",
        "bigtoast repo" at "http://bigtoast.github.io/repo/",
        "ticketfly repo" at "http://ticketfly.github.io/repo/"
      ),

      ivyXML :=
        <dependencies>
          <exclude org="com.sun.jmx" module="jmxri" />
          <exclude org="com.sun.jdmk" module="jmxtools" />
          <exclude org="javax.jms" module="jms" />
          <exclude org="thrift" module="libthrift" />
        </dependencies>,

      publishMavenStyle := false,
      publishTo := Some(Resolver.file("octo47.github.com", file(Path.userHome + "/Projects/github/octo47.github.com/repo"))),

      publishDocs <<= ( doc in Compile , target in Compile in doc, version ) map { ( docs, dir, v ) =>
        val newDir = Path.userHome / "/Projects/github/octo47.github.com/docs/async-zk-client" / v
        IO.delete( newDir )
        IO.createDirectory( newDir )
        IO.copyDirectory( dir, newDir )
      },

      libraryDependencies ++= dependencies

    ))
}
