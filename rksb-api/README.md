# API

**Module containing API related definitions, common and code generated libraries**


## Design

Modules intended to generate boilerplate code through layering of code generation steps as follows

1. Build the api domain configurations (e.g. definition of all requirements and configurations for code generation)
1. Run generator process to update the database schema and generate typesafe Java database schema


## Modules

Module          | Description
--------------- | ------------- 
[**`..`**](../README.md) | *parent*
[**`rksb-api-blueprint`**](./rksb-api-blueprint/README.md)    |  API blueprints, definitions and documentation
[**`rksb-api-common`**](./rksb-api-common/README.md)          |  API common custom code library (base classes, utilities)
[**`rksb-api-generator`**](./rksb-api-generator/README.md)    |  API database and code generation tooling
[**`rksb-api-service`**](./rksb-api-service/README.md)        |  API generated code output


## User Guide

Add the dependency to the code generated module in your `pom.xml`

```yaml
    <dependency>
        <groupId>me.roybailey</groupId>
        <artifactId>rksb-api-service</artifactId>
    </dependency>
```

Enable use of the blueprints datasource in your `application.xml`

```yaml
blueprints:
  datasource:
    # when enabled=true the primary dataSource is created from the application-blueprints.yml properties
    enabled: true
```

Enable use of the blueprints flyway data migration

```yaml
spring:
  flyway:
    # when enabled=true the spring database schema migration is applied to the flyway datasource
    enabled: false
blueprints:
  flyway:
    # when enabled=true the blueprints database schema migration is applied to the blueprints datasource
    enabled: true
```

## Developers Guide

Set the environment variable `BLUEPRINTS_GENERATOR=true` so that the generator runs as part of the local build process.
Do not set this for CI/CD builds.  The generated code is intended to be 'checked-in' after local testing and saved
as committed code.

* `mvn clean install`

### Prerequisites

* Local Postgres instance (running using Docker defaults)


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


