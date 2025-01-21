plugins {
    id("java")
}

group = "com.github.oleksandrkukotin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.scijava.org/content/repositories/public/")
}

val lwjglCoreVersion = "3.3.0"
val lwjglBindingsVersion = "3.3.0"
val glmVersion = "1.0.1"

dependencies {
    implementation("org.lwjgl:lwjgl:$lwjglCoreVersion")
    implementation("org.lwjgl.osgi:org.lwjgl.glfw:$lwjglBindingsVersion")
    implementation("org.lwjgl.osgi:org.lwjgl.opengl:$lwjglBindingsVersion")

    implementation("com.github.kotlin-graphics:glm:$glmVersion")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}