plugins {
    id("application")
}

group = "io.github.pc53"
version = "0.1.0-SNAPSHOT"

dependencies {
    implementation(project(":lib"))
}

application {
    mainClass.set("io.github.pc53.jwtservice.examples.Main")
}
