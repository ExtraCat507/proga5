plugins {
    id("java")
}

group = "org.xtracat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":Common"))

}

tasks.test {
    useJUnitPlatform()
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "org.xtracat.Main"
    }

    from(configurations.runtimeClasspath
        // .get() // uncomment this on Gradle 6+
        // .files
        .get()
        .map { if (it.isDirectory) it else zipTree(it) })
}
tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named<Javadoc>("javadoc") {
    source = fileTree("src/main/java") {
        include("**/*.java")
    }
    classpath = files(sourceSets.main.get().compileClasspath)

    options {
        encoding = "UTF-8"
    }
}