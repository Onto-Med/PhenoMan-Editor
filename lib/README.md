# Installation of PhenoMan in local Maven repository

1. Place a PhenoMan release inside this folder (e.g. version 0.3.3).
2. Run:
```shell script
mvn install:install-file -Dfile=lib/phenoman-0.3.3.jar -DgroupId=org.smith -DartifactId=phenoman -Dversion=0.3.3 -Dpackaging=jar
```