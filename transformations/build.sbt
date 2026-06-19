scalaVersion := "2.13.16"

val sparkVersion = "4.0.0"

lazy val root = (project in file("."))
  .settings(
    name := "transformations",
    idePackagePrefix := Some("com.veector"),
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-sql"  % sparkVersion,
      "org.scalameta"    %% "munit"       % "1.3.3" % Test
    ),
    fork := true, // required to run Spark locally via sbt
    javaOptions ++= Seq(
      "--add-opens", "java.base/javax.security.auth=ALL-UNNAMED",
      "--add-opens", "java.base/java.lang=ALL-UNNAMED"
    ),
  )
