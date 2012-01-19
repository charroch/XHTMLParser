name := "XHTMLHandler"

organization := "novoda.xml"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.7.1" % "test",
  "org.mockito" % "mockito-all" % "1.9.0" % "test"
)

resolvers ++= Seq("snapshots" at "http://scala-tools.org/repo-snapshots",
  "releases" at "http://scala-tools.org/repo-releases")