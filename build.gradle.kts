import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include

plugins {
    `java-library`
    `maven-publish`
   id("com.gorylenko.gradle-git-properties") version "2.4.1"
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.flywaydb.flyway") version "8.0.0"
    id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
    id("com.diffplug.spotless") version "6.23.0"
    jacoco
}
tasks.build {
    dependsOn(tasks.named("compileJava"))
    dependsOn(tasks.named("test"))
    dependsOn("bootJar")
}
tasks.test {
    useJUnitPlatform()
    exclude("com/rimalholdings/expensemanager/testInteg/*")


    maxHeapSize = "1g"
    testLogging {
        events("passed", "skipped", "failed")
    }
}
tasks.register(
    "testInteg",
    Test::class
) {
    useJUnitPlatform()
    include("com/rimalholdings/expensemanager/testInteg/*")
    maxHeapSize = "1g"
    testLogging {
        events("passed", "skipped", "failed")
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}
extra["springCloudVersion"] = "2023.0.0"


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.projectlombok:lombok")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    //implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-configuration-processor:2.6.2")
    implementation("org.springframework.boot:spring-boot-actuator-autoconfigure")
    // https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-netflix-eureka-client
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    // https://mvnrepository.com/artifact/net.logstash.logback/logstash-logback-encoder
    implementation ("net.logstash.logback:logstash-logback-encoder:7.4")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")
    runtimeOnly("com.mysql:mysql-connector-j")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.rest-assured:spring-mock-mvc:5.4.0")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")// Use the latest version


    //integTestImplementation(sourceSets.test.get().output)
    //integTestImplementation(configurations.testImplementation.get())
   // integTestRuntimeOnly(configurations.testRuntimeOnly.get())
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
jacoco {
    toolVersion = "0.8.11"
    reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report

    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

afterEvaluate {
    tasks.jacocoTestReport {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it) {
                exclude("com/rimalholdings/expensemanager/config/**")
                exclude("com/rimalholdings/expensemanager/data/**")
                exclude("com/rimalholdings/expensemanager/exception/**")
                exclude("com/rimalholdings/controller/apiError/*Handler")


            }
        }))
    }
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}
tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.named("compileJava", JavaCompile::class) {
    options.compilerArgs.add("-parameters")
}
tasks.named("compileTestJava", JavaCompile::class) {
    options.compilerArgs.add("-parameters")
}