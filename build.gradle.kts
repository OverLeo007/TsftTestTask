plugins {
    id("java")
    id("application")
    //https://plugins.gradle.org/plugin/io.freefair.lombok
    id("io.freefair.lombok") version "8.11"

}

group = "ru.paskal"
version = "1.0"

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.annotationProcessorPath = configurations.annotationProcessor.get()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    implementation("org.slf4j:slf4j-api:2.0.16")

    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:1.5.12")

    // https://projectlombok.org/setup/gradle
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    compileOnly("org.projectlombok:lombok:1.18.36")

    // https://mvnrepository.com/artifact/commons-cli/commons-cli
    implementation("commons-cli:commons-cli:1.9.0")

    //https://docs.gradle.org/8.8/userguide/upgrading_version_8.html#test_framework_implementation_dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    exclude( "**/inputs/**")
}


tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.paskal.Main"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    destinationDirectory.set(file("${layout.buildDirectory.get()}/outputJar"))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set("file_util")
}
