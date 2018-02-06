import org.gradle.api.JavaVersion.*
import org.gradle.api.tasks.wrapper.Wrapper.*
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.*
import org.gradle.kotlin.dsl.*
import java.io.File
import java.lang.Runtime.*
plugins {
    id("biz.aQute.bnd.builder")/*substitutes `java`*/version "3.3.0" // "4.0.0-SNAPSHOT" for real {@see settings.gradle.kts}
}
group = extra["projectGroup"]!!
version = extra["projectVersion"] as String
repositories {
    //extra["mavenAddRepo"].toString().split(" ").forEach { maven(url = it) }
    maven( url = "https://oss.sonatype.org/content/groups/osgi/")
    jcenter()
}
java { sourceCompatibility = VERSION_1_8 }
tasks {
        withType<JavaCompile> {
            options.apply {
                encoding = "UTF-8"
                isIncremental = true
				/* 
                 * compiler deamon JVM options
				 * omitted for this demo
                 */
                isFork = true
                /* 
                 * javac tasks args
                 */
				// TODO: @argFile does does not work for some reason. Bug ? 
                //compilerArgs.add("@" + project.rootDir.absolutePath.replace(Regex("\\s+"), " \"\$1\"").replace('\\', '/') + "/javac" + current().majorVersion + "Args")
                File(project.rootDir.absolutePath + "/javac"+current().majorVersion+"Args").useLines { lines -> lines.forEach { compilerArgs.add(it) }}
            }
        }
        withType<Test> {
            testLogging.showStandardStreams = project.extra["isShowStdStreamsOnTests"].toString().toBoolean()
            maxParallelForks = getRuntime().availableProcessors()
            forkEvery = 100
            reports.html.isEnabled = false
            reports.junitXml.isEnabled = false
        }
        withType<Wrapper> {
			description = "Generating the Gradle wrapper scripts"
            gradleVersion = project.extra["gradleWrapperVersion"] as String
            distributionType = ALL
            distributionUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-all.zip"
        }
}
dependencies {
    compileOnly("org.osgi", "osgi.core", "6.0.0")
    compileOnly("org.osgi", "osgi.cmpn", "6.0.0")
    compileOnly("org.osgi", "osgi.enterprise", "6.0.0")
    compileOnly("org.osgi", "org.osgi.annotation.versioning", "1.0.0")
	
    implementation("org.apache.aries.blueprint", "org.apache.aries.blueprint.cm", "1.1.0")
    implementation("org.apache.aries.transaction", "org.apache.aries.transaction.blueprint", "2.1.0")
    implementation("org.hibernate", "hibernate-core", "5.2.11.Final") {
        exclude("org.javassist", "javassist")
    }
	// for las version 2.3.0 on JDK 9 bnd complains adout classpath is wrong due to multirelease jar  
    implementation("javax.xml.bind", "jaxb-api", "2.3.0-b170201.1204")

    testImplementation("junit", "junit", "4.12")

}