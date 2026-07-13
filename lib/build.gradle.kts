plugins {
    id("java-library")
}

group = "io.github.pc53"
version = "0.1.0-SNAPSHOT"

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
