buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("net.serenity-bdd:serenity-gradle-plugin:4.0.1")
    }
}

plugins {
    java
    idea
}

apply(plugin = "net.serenity-bdd.serenity-gradle-plugin")

group = "co.com.vetclinica"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val serenityVersion = "4.0.1"
val serenityCucumberVersion = "5.3.1"

dependencies {
    // Aserciones
    implementation("org.hamcrest:hamcrest:2.2")

    // Serenity BDD + Cucumber + Screenplay
    implementation("net.serenity-bdd:serenity-cucumber:$serenityCucumberVersion")
    implementation("net.serenity-bdd:serenity-core:$serenityVersion")
    implementation("net.serenity-bdd:serenity-junit:$serenityVersion")
    implementation("net.serenity-bdd:serenity-screenplay:$serenityVersion")
    implementation("net.serenity-bdd:serenity-screenplay-webdriver:$serenityVersion")
    implementation("net.serenity-bdd:serenity-ensure:2.0.49")

    // Logging
    implementation("org.slf4j:slf4j-simple:2.0.18")

    // Runner JUnit 4 (CucumberWithSerenity)
    testImplementation("junit:junit:4.13.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnit()
    // Un solo hilo: garantiza el orden secuencial de los escenarios Cucumber
    maxParallelForks = 1
    systemProperty("cucumber.publish.quiet", "true")
    // Apertura de modulos requerida por Serenity/Selenium en JDK modernos
    jvmArgs(
        "--add-opens", "java.base/java.lang=ALL-UNNAMED",
        "--add-opens", "java.base/java.util=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED"
    )
    // Genera el reporte HTML agregado al finalizar las pruebas
    finalizedBy("aggregate")
}
