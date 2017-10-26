[![Build Status](https://travis-ci.org/amv-networks/amv-vertx.svg?branch=master)](https://travis-ci.org/amv-networks/amv-vertx)
[![License](https://img.shields.io/github/license/amv-networks/amv-vertx.svg?maxAge=2592000)](https://github.com/amv-networks/amv-vertx/blob/master/LICENSE)


amv-vertx
========


# build
Build a snapshot from a clean working directory
```bash
$ ./gradlew releaseCheck clean build -Prelease.stage=SNAPSHOT -Prelease.scope=patch
```

When a parameter `minimal` is provided, certain tasks will be skipped to make the build faster.
e.g. `findbugs`, `checkstyle`, `javadoc` - tasks which results are not essential for a working build.
```bash
./gradlew clean build -Pminimal
```

## publish SNAPSHOT to local maven repository
```
./gradlew clean build -Pminimal -Prelease.stage=SNAPSHOT -Prelease.scope=patch publishToMavenLocal
```

## create a release
```bash
./gradlew final -Prelease.scope=patch
```

## release to bintray
```bash
./gradlew clean build final bintrayUpload
  -Prelease.useLastTag=true
  -PreleaseToBintray
  -PbintrayUser=${username}
  -PbintrayApiKey=${apiKey}
```

# license
The project is licensed under the Apache License. See [LICENSE](LICENSE) for details.
