
plugins {
    `java-library`
    `maven-publish`
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.flywaydb.flyway") version "8.0.0"
    id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
    id("com.diffplug.spotless") version "6.23.0"
}


tasks.build {
    dependsOn(tasks.named("compileJava"))
    dependsOn(tasks.named("test"))
    dependsOn("bootJar")
}


repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.projectlombok:lombok")
    implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-configuration-processor:2.6.2")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")
    implementation("com.mysql:mysql-connector-j:8.2.0")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    compileOnly ("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testImplementation( "io.projectreactor:reactor-test")
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
}

group = "com.rimalholdings"
version = "0.0.1-SNAPSHOT"
description = "expenseManager"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

openApi {
    apiDocsUrl.set("http://localhost:8080/v3/api-docs")
    outputDir.set(file("."))
}
spotless {
    java {
        // optional: you can specify a specific version and/or config file
        removeUnusedImports()
        googleJavaFormat()
        indentWithTabs()
        trimTrailingWhitespace()
        endWithNewline()
        importOrder("java|javax", "com.rimalholdings", "", "\\#com.rimalholdings", "\\#")


    }
}
openApi {
    apiDocsUrl.set("http://localhost:8080/v3/api-docs") // URL of your API docs
    outputDir.set(file(".")) // Output directory
    outputFileName.set("swagger.yml") // Output file name
}
flyway {
  configFiles = arrayOf("flyway.conf")

}
