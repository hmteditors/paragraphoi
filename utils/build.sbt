scalaVersion := "2.11.8"

resolvers += Resolver.jcenterRepo
//resolvers += Resolver.bintrayRepo("neelsmith","maven")
libraryDependencies ++=   Seq(
  "edu.holycross.shot.cite" %% "xcite" % "3.2.1",
  "edu.holycross.shot" %% "ohco2" % "10.4.0"
)
