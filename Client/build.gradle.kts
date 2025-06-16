plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.xtracat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "17.0.10"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":Common"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("org.xtracat.UI.AppStarter")
}

tasks.withType<ProcessResources> {
    filteringCharset = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

// 4. Configure the final JAR assembly
tasks.withType<Jar> {

    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }

    // Add the converted i18n files from our task's output directory
    from(layout.buildDirectory.dir("resources-n2a/i18n"))

    // Unpack all dependency JARs as before
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}