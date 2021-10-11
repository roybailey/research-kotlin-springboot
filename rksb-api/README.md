# API

**Module containing API related definitions, common and code generated libraries**


## Design

Modules to generate boilerplate code for supporting RESTful APIs mapped to database tables.

* Blueprint templates are used to define modular and high level Controller, Service, Table and Data Model mappings
* Blueprint templates are compiled into a single aggregate blueprint collection
* The blueprint compiler populates derived values (e.g. package names, class names) and fields taken from a database table
* The generated code is checked-in to allow for the same audit trail and diff comparisons as the blueprints grow or evolve
* The generated code is part of the maven build but only executes when enabled (intended for local development only)
* Flyway is used to control an idempotent database schema used to generate code at (local) build time before being checked-in


## Modules

Module          | Description
--------------- | ------------- 
[**`..`**](../README.md) | *parent*
[**`rksb-api-blueprint`**](./rksb-api-blueprint/README.md)    |  API blueprint templates and runtime SpringBoot configuration
[**`rksb-api-common`**](./rksb-api-common/README.md)          |  API common code library (base classes, utilities)
[**`rksb-api-generator`**](./rksb-api-generator/README.md)    |  API database and code generation tool
[**`rksb-api-service`**](./rksb-api-service/README.md)        |  API service generated code output


## User Guide

Add the dependency to the code generated module in your `pom.xml`

```yaml
    <dependency>
        <groupId>me.roybailey</groupId>
        <artifactId>rksb-api-service</artifactId>
    </dependency>
```

Enable use of the blueprint datasource in your `application.xml`

```yaml
blueprint:
  datasource:
    # when enabled=true the primary dataSource is created from the application-blueprint.yml properties
    enabled: true
```

Enable use of the blueprint flyway data migration (typically disable for auto-configured spring)

```yaml
spring:
  flyway:
    # when enabled=true the spring database schema migration is applied to the flyway datasource
    enabled: false
blueprint:
  flyway:
    # when enabled=true the blueprint database schema migration is applied to the blueprint datasource
    enabled: true
```

## Developers Guide

Set the environment variable `BLUEPRINT_GENERATOR=true` so that the generator runs as part of the local build process.
Do not set this for CI/CD builds.  The generated code is intended to be 'checked-in' after local testing and saved
as committed code.

* `mvn clean install`

### Prerequisites

* Local Postgres instance (running using Docker defaults)


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


