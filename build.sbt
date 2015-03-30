lazy val projectPrefix = """lockcrypto-"""
lazy val commonSettings = Seq(
  version := "0.01-SNAPSHOT",
  scalaVersion := "2.11.6"
)

lazy val root = (project in file(".")).
  aggregate(core, web, nicsCrypto, jpbcAPI, jpbcPlaf)

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

lazy val nicsCrypto = project.
  dependsOn(jpbcAPI, jpbcPlaf).
  settings(commonSettings: _*).
  settings(
    name := """nics-crypto"""
  )

lazy val jpbcSettings = Seq(
  version := "2.0.0"
)

lazy val jpbcAPI = project.
  settings(commonSettings: _*).
  settings(jpbcSettings: _*).
  settings(
    name := """jpbc-api"""
  )

lazy val jpbcPlaf = project.
  dependsOn(jpbcAPI).
  settings(commonSettings: _*).
  settings(jpbcSettings: _*).
  settings(
    name := """jpbc-plaf"""
  )
