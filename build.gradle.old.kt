/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    mavenCentral()
}

dependencies {
    api(libs.org.springframework.boot.spring.boot.starter.actuator)
    api(libs.org.springframework.boot.spring.boot.starter.data.jpa)
    api(libs.org.springframework.boot.spring.boot.starter.web)
    api(libs.org.projectlombok.lombok)
    api(libs.org.springdoc.springdoc.openapi.starter.webmvc.ui)
    api(libs.org.springframework.boot.spring.boot.starter.oauth2.resource.server)
    api(libs.org.springframework.boot.spring.boot.configuration.processor)
    api(libs.org.flywaydb.flyway.core)
    api(libs.org.flywaydb.flyway.mysql)
    runtimeOnly(libs.com.mysql.mysql.connector.j)
    runtimeOnly(libs.org.springframework.boot.spring.boot.docker.compose)
    testImplementation(libs.org.springframework.boot.spring.boot.starter.test)
    implementation(kotlin("stdlib-jdk8"))
}

group = "com.rimalholdings"
version = "0.0.1-SNAPSHOT"
description = "expenseManager"

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}
kotlin {
    jvmToolchain(17)
}