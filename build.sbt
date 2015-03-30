lazy val projectPrefix = """lockcrypto-"""
lazy val commonSettings = Seq(
  version := "0.01-SNAPSHOT",
  scalaVersion := "2.11.6"
)

lazy val root = (project in file(".")).
  aggregate(core, web)

lazy val core = project.
  settings(commonSettings: _*).
  settings(
    name := projectPrefix + """core"""
  )

lazy val web = project.
  dependsOn(core).
  enablePlugins(PlayScala).
  settings(commonSettings: _*).
  settings(
    name := projectPrefix + """web""",
    libraryDependencies ++= Seq(
      jdbc,
      anorm,
      cache,
      ws
    )
  )

