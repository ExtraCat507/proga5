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
    // https://mvnrepository.com/artifact/com.thoughtworks.xstream/xstream
    implementation("com.thoughtworks.xstream:xstream:1.4.21")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.18.3")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-xml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.3")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.3")
    implementation("org.postgresql:postgresql:42.7.2")
}

tasks.test {
    useJUnitPlatform()
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "org.xtracat.client.Main"
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