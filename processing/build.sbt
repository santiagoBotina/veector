scalaVersion := "2.12.18"

val sparkVersion = "3.5.3"
val hadoopVersion = "3.3.4"

assembly / assemblyJarName := "veector-processing.jar"

lazy val root = (project in file("."))
  .enablePlugins(AssemblyPlugin)
  .settings(
    name := "processing",
    idePackagePrefix := Some("com.veector"),
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-sql"  % sparkVersion % "provided",
      "org.scalameta"    %% "munit"       % "1.3.3" % Test,
      "org.apache.hadoop" % "hadoop-aws" % hadoopVersion,
      "com.amazonaws" % "aws-java-sdk-bundle" % "1.12.262"
    ),
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case _ => MergeStrategy.first
    },
    fork := true, // required to run Spark locally via sbt
    javaOptions ++= Seq(
      "--add-opens", "java.base/javax.security.auth=ALL-UNNAMED",
      "--add-opens", "java.base/java.lang=ALL-UNNAMED"
    ),
  )
