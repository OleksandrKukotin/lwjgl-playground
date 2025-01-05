plugins {
    id("java")
}

group = "com.github.oleksandrkukotin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val lwjglCoreVersion = "3.3.0"
val lwjglBindingsVersion = "3.3.0"
val ffMpegVersion = "7.0.1-1.2"

dependencies {
    implementation("org.lwjgl:lwjgl:$lwjglCoreVersion")
    implementation("org.lwjgl.osgi:org.lwjgl.glfw:$lwjglBindingsVersion")
    implementation("org.lwjgl.osgi:org.lwjgl.opengl:$lwjglBindingsVersion")

    implementation("com.nextbreakpoint:com.nextbreakpoint.ffmpeg4java:$ffMpegVersion")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}