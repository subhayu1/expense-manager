
plugins {
    `java-library`
    `maven-publish`
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.flywaydb.flyway") version "10.4.1"
    id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
    id("com.diffplug.spotless") version "6.0.0"
}
//	id 'io.spring.dependency-management' version '1.1.3'
//	id "org.flywaydb.flyway" version "8.0.0"
//	id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
//	id("com.diffplug.spotless") version "6.23.3"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.2")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.projectlombok:lombok:1.18.22")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.3")
    implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-configuration-processor:2.6.2")
    implementation("org.flywaydb:flyway-core:10.4.1")
    implementation("org.flywaydb:flyway-mysql:10.4.1")
    // https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
    implementation("com.mysql:mysql-connector-j:8.2.0")


    developmentOnly ("org.springframework.boot:spring-boot-docker-compose")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    compileOnly ("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok:1.18.22")

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
    apiDocsUrl.set("http://localhost:8080/v3/api-docs") // URL of your API docs
    outputDir.set(file(".")) // Output directory
    outputFileName.set("swagger.yml") // Output file name
}
spotless {
    // optional: limit format enforcement to just the files changed by this feature branch
    // ratchetFrom("origin/main")

    format("misc") {
        // define the files to apply `misc` to
        target("*.gradle", "*.md", ".gitignore")

        // define the steps to apply to those files
        trimTrailingWhitespace()
        indentWithSpaces(3) // or spaces. Takes an integer argument if you don't like 4
        endWithNewline()
        setEncoding("UTF-8")
    }
    java {
        eclipse() // I like eclipse formatting over google
        // googleJavaFormat("1.11.0").aosp().reflowLongStrings() // this is the other option
    }
}