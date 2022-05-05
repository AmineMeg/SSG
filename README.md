# SSG (Team Rocket)

|         | Pipeline                                                                                                                                                                                                   | Coverage                                                                                                                                                                                                   |
|---------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| master  | [![pipeline status (master)](https://gaufre.informatique.univ-paris-diderot.fr/meguenni/ssg/badges/master/pipeline.svg)](https://gaufre.informatique.univ-paris-diderot.fr/meguenni/ssg/commits/master)    | [![coverage report (master)](https://gaufre.informatique.univ-paris-diderot.fr/meguenni/ssg/badges/master/coverage.svg)](https://gaufre.informatique.univ-paris-diderot.fr/meguenni/ssg/commits/master)    |
| develop | [![pipeline status (develop)](https://gaufre.informatique.univ-paris-diderot.fr/meguenni/ssg/badges/develop/pipeline.svg)](https://gaufre.informatique.univ-paris-diderot.fr/meguenni/ssg/commits/develop) | [![coverage report (develop)](https://gaufre.informatique.univ-paris-diderot.fr/meguenni/ssg/badges/develop/coverage.svg)](https://gaufre.informatique.univ-paris-diderot.fr/meguenni/ssg/commits/develop) |

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
SSG is a static site generator written in Java.
This tools aims to create HTML page from your content source written in Markdown.

## Technologies
Project is created with:
* Java : 18
* Gradle : 7.4.2

And uses :
* JUnit : 5.8.1
* JaCoCo : 0.8.8
* Checkstyle : 10.0 with a customized configuration file inspired by Google's [checkstyle.xml](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml). You can check here the complete rules that they use : https://google.github.io/styleguide/javaguide.html#s3.4.1-one-top-level-class
* PMD : 6.42.0
* Spotbugs : 5.0.6
* test-logger : 3.1.0
* Log4j2 : 2.17.2
* Google Guice : 5.1.0
* Picocli : 4.6.3
* CommonMark : 0.18.2
* Mockito : 4.4.0
* unirest-java: 3.13.6
* shadowjar: 7.1.2
* tomlj: 1.0.0
* jackson: 2.13.2
* jinjava: 2.6.0

## Setup
To build this project, you can use gradlew

```bash
$ ./gradlew build
```

## Other commands

```bash
// To run all unit tests
$ ./gradlew test

// To run all code checking
$ ./gradlew check 
```

To run any more specific task check the build.gradle file.

## Presentation video (Click on any link) 

* [Pitchy](https://app.pitchy.fr/share?id=vodPNeLo)
* [Youtube](https://youtu.be/SFpZN9Tsj2w)
