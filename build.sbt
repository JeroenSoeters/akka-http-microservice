enablePlugins(JavaAppPackaging)

name         := "manufacturing-microservice"
organization := "P&G"
version      := "1.0"
scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV         = "2.4.2"
  val akkaStreamV   = "2.0.3"
  val scalaTestV    = "2.2.5"
  val kafkaV        = "0.10.0"
  val scalaLoggingV = "3.1.0"
  val logbackV      = "1.1.2"
  val slf4jV        = "2.3.6"
  Seq(
    "com.typesafe.scala-logging"     %% "scala-logging"                        % scalaLoggingV,
    "com.typesafe.akka"              %% "akka-slf4j"                           % slf4jV,
    "ch.qos.logback"                 % "logback-classic"                       % logbackV,
    "com.softwaremill.reactivekafka" %% "reactive-kafka-core"                  % kafkaV,
    "com.typesafe.akka"              %% "akka-actor"                           % akkaV,
    "com.typesafe.akka"              %% "akka-stream-experimental"             % akkaStreamV,
    "com.typesafe.akka"              %% "akka-http-core-experimental"          % akkaStreamV,
    "com.typesafe.akka"              %% "akka-http-experimental"               % akkaStreamV,
    "com.typesafe.akka"              %% "akka-http-spray-json-experimental"    % akkaStreamV,
    "com.typesafe.akka"              %% "akka-http-testkit-experimental"       % akkaStreamV,
    "org.scalatest"                  %% "scalatest"                            % scalaTestV % "test"
  )
}

lazy val cleanBuild = TaskKey[Unit]("clean-build", "Cleans code and builds project. Note clean will take more time than normal sbt build.")
cleanBuild := Def.sequential(clean, compile in Compile, compile in Test).value

lazy val buildZip = TaskKey[Unit]("build-zip", "Publishes a zip file with the new code.")
lazy val deployService = TaskKey[Unit]("deploy-service", "Builds and deploys a Docker container of the service.")

buildZip <<= ((packageBin in Universal) map { out =>
  println("Copying Zip file to docker directory.")
  IO.move(out, file(out.getParent + "/../../docker/" + out.getName))
})
buildZip <<= buildZip.dependsOn(cleanBuild)

deployService := ("./refresh-service.sh"!)
deployService <<= deployService.dependsOn(buildZip)

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"

Revolver.settings
