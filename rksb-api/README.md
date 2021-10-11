# API

**Module to generate boilerplate code for supporting RESTful APIs mapped to database tables.**


## Design

To define a small 'blueprint' configuration that can be expanded into a full configuration sufficient to create
controllers mapped to services that are mapped to tables and data models,
for serving table data via a RESTful API with filtering capabilities.

* Blueprint templates are used to define modular and high level Controller, Service, Table and Data Model mappings
* Blueprint templates are compiled into a single aggregate blueprint collection for code-generation and runtime loading
* Derive as many blueprint values as possible while allowing for overrides
* Examples of derived values include: package names, class names, and fields taken from a database table and used for models and filtering
* The generated code is checked-in and therefore under the same source code control as manual code
* The code generation is treated as a compilation step, this is by design and deliberate, allowing for safe control and manual intervention if needed
* The code generation is part of the maven build but only executes when enabled for local development (not intended for CI/CD builds)
* Flyway is used to control an idempotent database schema used to generate code at (local) build time and for APIs to access at runtime


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

### Flyway Database Schema Migration

The code generator and manager service apply the database schema changes for APIs at build and deployment time respectively.

Should another service need to action this it can do so by adding the Flyway dependency in their `pom.xml`

```xml
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
    </dependency>
```
and enabling the use of the blueprint flyway data migration in their `application.yml` config

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

Note: typically you will need to disable the Spring Flyway auto-configuration as above unless required separately.


## Developers Guide

Set the environment variable `BLUEPRINT_GENERATOR=true` so that the generator runs as part of the local build process.

**Do not set this for CI/CD builds.**

The generated code is intended to be 'checked-in' after local testing and saved as committed code.

* `mvn clean install`


### Prerequisites

* Local Postgres instance (running using Docker defaults)


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


