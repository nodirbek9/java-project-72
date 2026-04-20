plugins {
    id("jacoco")
    application
    checkstyle
    id("org.sonarqube") version "7.1.0.6387"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

application {
    mainClass = "hexlet.code.App"
}

sonar {
    properties {
        property("sonar.projectKey", "nodirbek9_java-project-72");
        property("sonar.organization", "nodirbek9")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "build/reports/jacoco/test/jacocoTestReport.xml"
        )
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

checkstyle {
    toolVersion = "10.12.4"
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
}

dependencies {
    // Databases
    implementation("com.h2database:h2:2.3.232")
    implementation("com.zaxxer:HikariCP:6.3.0")
    implementation("org.postgresql:postgresql:42.1.4")
    //Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("org.apache.commons:commons-text:1.13.1")
    implementation("gg.jte:jte:3.2.0")
    implementation("org.projectlombok:lombok:0.11.0")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("io.javalin:javalin:6.6.0")
    implementation("io.javalin:javalin-bundle:6.6.0")
    implementation("io.javalin:javalin-rendering:6.6.0")

    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation(platform("org.junit:junit-bom:5.12.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
group = "hexlet.code"
version = "1.0-SNAPSHOT"