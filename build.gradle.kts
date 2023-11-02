group = "org.jetbrains.edu"
version = "1.0"

plugins {
    kotlin("jvm") version "1.9.20"
    id("io.gitlab.arturbosch.detekt") version "1.23.3"
    id("org.cqfn.diktat.diktat-gradle-plugin") version "1.2.3"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.15.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testImplementation("org.junit.platform:junit-platform-console:1.9.0")
}

tasks.test {
    useJUnitPlatform()
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$projectDir/config/detekt.yml")
}

diktat {
    inputs {
        include("src/**/*.kt")
        exclude("src/test/**")
    }
    diktatConfigFile = file("$projectDir/config/diktat.yml")
}

tasks.register("diktat") {
    group = "verification"
    dependsOn(tasks.getByName("diktatCheck"))
}

application {
    mainClass.set("org.jetbrains.edu.wikirace.MainKt")
}

tasks.getByName<JavaExec>("run") {
    standardInput = System.`in`
}
