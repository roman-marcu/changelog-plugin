# changelog-plugin

Build
status: [![CircleCI](https://dl.circleci.com/status-badge/img/circleci/PQAU9FtZdPMR7NgGJGyAq4/YDrYVcVhR7xyCnV2opV1t2/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/circleci/PQAU9FtZdPMR7NgGJGyAq4/YDrYVcVhR7xyCnV2opV1t2/tree/main)

## Test usage

```
./mvnw dev.romanmarcu:changelog-plugin:git-changelog -Dtemplate="<your path>
/changelog-plugin/src/main/resources/templates/CHANGELOG.md.ftl" -Doutput="CHANGELOG.md"

```

### More info

Plugin to parse and create a changelog file from all the commits using https://www.conventionalcommits.org/en/v1.0.0/
