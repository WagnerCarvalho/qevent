import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.tasks.LintTask
import org.jmailen.gradle.kotlinter.tasks.FormatTask

apply(plugin = "org.jmailen.kotlinter")

plugins {
	id("org.springframework.boot") version "2.2.2.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	id("org.jmailen.kotlinter") version "2.1.2"
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
}

buildscript {
	repositories {
		maven {
			url = uri("https://plugins.gradle.org/m2/")
		}
	}
	dependencies {
		classpath("org.jmailen.gradle:kotlinter-gradle:2.2.0")
	}
}

group = "com.qevent"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.squareup.okhttp3:logging-interceptor:3.10.0")
	implementation("io.reactivex.rxjava2:rxjava:2.2.5")
	implementation("com.squareup.retrofit2:retrofit:2.4.0")
	implementation("com.squareup.retrofit2:converter-jackson:2.4.0")
	implementation("com.squareup.retrofit2:adapter-rxjava2:2.4.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation ("org.mockito:mockito-core:2.8.9")
	testImplementation ("com.squareup.retrofit2:retrofit-mock:2.0.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

sourceSets["main"].java.srcDir("src/main/kotlin")

kotlinter {
	ignoreFailures = false
	indentSize = 4
	continuationIndentSize = 4
	reporters = arrayOf("checkstyle", "plain")
	experimentalRules = false
	disabledRules = arrayOf(
		"filename",
		"import-ordering"
	)
	fileBatchSize = 30
}

val ktLint by tasks.creating(LintTask::class) {
	group = "verification"
	source(files("src"))
	reports = mapOf(
		"plain" to file("build/lint-report.txt"),
		"json" to file("build/lint-report.json")
	)
}

val ktFormat by tasks.creating(FormatTask::class) {
	group = "formatting"
	source(files("src"))
	report = file("build/format-report.txt")
}
