plugins {
    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {

    implementation("net.coobird:thumbnailator:0.4.14")
    implementation("org.apache.pdfbox:pdfbox:2.0.24") 
}

application {
    mainClass.set("bulkimageresizeandconvert.Main")
}

// tasks.jar {
//     manifest {
//         attributes["Main-Class"] = "bulkimageresizeandconvert.Main"
//     }
//     from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
// }

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

