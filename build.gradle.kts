plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("org.jetbrains.kotlin.kapt") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.micronaut.application") version "2.0.3"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.5.21"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.5.21"
}

allOpen {
    annotation("io.micronaut.http.annotation.Controller")
}

version = "0.1"
group = "br.com.zupacademy.giovannimoratto"

val kotlinVersion = project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("br.com.zupacademy.giovannimoratto.*")
    }
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    kapt("io.micronaut.data:micronaut-data-processor")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("javax.annotation:javax.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut:micronaut-validation")

    // Hibernate
    implementation("org.hibernate:hibernate-validator:6.1.6.Final")
    implementation("io.micronaut.beanvalidation:micronaut-hibernate-validator")

    // JPA
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")

    // Database
    implementation("org.postgresql:postgresql")

    // Jackson XML
    implementation("io.micronaut.xml:micronaut-jackson-xml")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Tests
    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.mockito:mockito-core")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("com.h2database:h2")
    runtimeOnly("com.h2database:h2")

    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

}


application {
    mainClass.set("br.com.zupacademy.giovannimoratto.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("16")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "16"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "16"
        }
    }


}
