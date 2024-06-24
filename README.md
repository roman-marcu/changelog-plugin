changelog-plugin
====================

![GitHub License](https://img.shields.io/github/license/roman-marcu/changelog-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/dev.romanmarcu/changelog-plugin.svg?label=Maven%20Central)](https://search.maven.org/artifact/dev.romanmarcu/changelog-plugin)
[![CircleCI](https://dl.circleci.com/status-badge/img/circleci/PQAU9FtZdPMR7NgGJGyAq4/YDrYVcVhR7xyCnV2opV1t2/tree/main.svg?style=shield)](https://dl.circleci.com/status-badge/redirect/circleci/PQAU9FtZdPMR7NgGJGyAq4/YDrYVcVhR7xyCnV2opV1t2/tree/main)

Description
---------------
You have found a bug or you have an idea for a cool new feature? Just contribute.

All the commit that will not follow the format will be ignored.

Test usage
---------------

```
./mvnw dev.romanmarcu:changelog-plugin:git-changelog -Dtemplate="<your path>
/changelog-plugin/src/main/resources/templates/CHANGELOG.md.ftl" -Doutput="CHANGELOG.md"
```

More info
---------------
Plugin to parse and create a changelog file from all the commits using https://www.conventionalcommits.org/en/v1.0.0/
