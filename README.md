changelog-plugin
====================

![GitHub License](https://img.shields.io/github/license/roman-marcu/changelog-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.roman-marcu/changelog-plugin.svg?label=Maven%20Central)](https://search.maven.org/artifact/dev.romanmarcu/changelog-plugin)
[![CircleCI](https://dl.circleci.com/status-badge/img/gh/roman-marcu/changelog-plugin/tree/main.svg?style=shield)](https://dl.circleci.com/status-badge/redirect/gh/roman-marcu/changelog-plugin/tree/main)

Description
---------------
You have found a bug or you have an idea for a cool new feature? Just contribute.


Test usage
---------------

```
./mvnw io.github.roman-marcu:changelog-plugin:git-changelog -Dtemplate="<your path>
/changelog-plugin/src/main/resources/templates/CHANGELOG.md.ftl" -Doutput="CHANGELOG.md"
```

More info
---------------
Plugin to parse and create a changelog file from all the commits using https://www.conventionalcommits.org/en/v1.0.0/

Deploy
---------------
https://github.com/introproventures/releaser
