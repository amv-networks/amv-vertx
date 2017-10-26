[![Build Status](https://travis-ci.org/amvnetworks/amv-vertx.svg?branch=master)](https://travis-ci.org/amvnetworks/amv-vertx)

amv-vertx
========

# build
```
./gradlew clean build
```

###### publish SNAPSHOT to local maven repository
```
./gradlew clean build -Pminimal -Prelease.stage=SNAPSHOT -Prelease.scope=patch publishToMavenLocal
```

# license
The project is licensed under the Apache License. See [LICENSE](LICENSE) for details.
