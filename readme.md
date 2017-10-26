[![Build Status](https://travis-ci.org/amv-networks/amv-vertx.svg?branch=master)](https://travis-ci.org/amv-networks/amv-vertx)
[![License](https://img.shields.io/github/license/amv-networks/amv-vertx.svg?maxAge=2592000)](https://github.com/amv-networks/amv-vertx/blob/master/LICENSE)


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
