plugins {
    id("java")
}

group = "com.github.oleksandrkukotin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val lwjglVersion = "3.1.0"
dependencies {
    implementation("org.lwjgl:lwjgl:$lwjglVersion")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}