name := "play-email-list"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaCore,
  javaWs % "test",
  "com.h2database" % "h2" % "1.4.181",
  "org.webjars" % "bootstrap" % "2.1.1",
  "org.springframework" % "spring-context" % "4.1.1.RELEASE",
  "org.springframework" % "spring-orm" % "4.1.1.RELEASE",
  "org.springframework" % "spring-jdbc" % "4.1.1.RELEASE",
  "org.springframework" % "spring-tx" % "4.1.1.RELEASE",
  "org.springframework" % "spring-expression" % "4.1.1.RELEASE",
  "org.springframework" % "spring-aop" % "4.1.1.RELEASE",
  "org.springframework" % "spring-test" % "4.1.1.RELEASE" % "test",
  "org.hibernate" % "hibernate-entitymanager" % "5.0.7.Final",
  "mysql" % "mysql-connector-java" % "5.1.35",
  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.12"
)

libraryDependencies += "org.webjars" % "jquery" % "1.11.2"

lazy val root = (project in file(".")).enablePlugins(PlayJava)
