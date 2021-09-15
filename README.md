# Research Kotlin SpringBoot

**Multi-Module Kotlin SpringBoot Project**

**STATUS** _Under Construction_


## Design

To create a template Kotlin project for generating API boilerplate code.

* Use multi-modules to create common libraries and 'starter' projects that encapsulate code and dependencies
* Create demo SpringBoot application to pull everything together and provide working template


## Modules

Module          | Description
--------------- | ------------- 
[**`rksb-common`**](./rksb-common/README.md)    |  Common libraries and starter packs defining language, logging, testing and 3rd party dependencies
[**`rksb-api`**](./rksb-api/README.md)          |  API definition and code generation
[**`rksb-service`**](./rksb-service/README.md)  |  Service examples


## Getting Started

* `mvn clean install`
* `./demo.sh` to run the demo application

### Prerequisites

* **Java11**, this project contains 3rd party libraries that require Java11 or above 
* **Postgres** access for code generation, see `pom.xml` for properties to assign
* **Docker**, optionally used by test-containers for testing 3rd party databases (excluded by default using JUnit5 tag `test-containers`) 

```
docker run --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=localhost postgres
```

### Adding 3rd Party Dependency

Versions of 3rd party dependencies used more than once such as logging, spring, utilities 
(i.e. not isolated to specialist library) are defined as a property in the main **`pom.xml`**

They are then declared using that version property in the **Bill-of-Materials** `pom.xml` for all other
modules and services to inherit.  This keeps the version definition in one place.

* Update [**`pom.xml`**](./pom.xml) with new dependency library version as a property
* Update [**Bill-of-Materials**](./rksb-common/rksb-common-bom/pom.xml) with version


### Adding New Common Library

Any specialist software should be confined to a common library module within 
[**Common Libraries**](./rksb-common/rksb-common-lib/pom.xml) `pom.xml`

* Add new module project to the [**Common Libraries**](./rksb-common/rksb-common-lib/) project
* Define required dependencies in the new libraries `pom.xml` 
* Add the new common library module to the [**Common Libraries**](./rksb-common/rksb-common-lib/pom.xml) `pom.xml`


### Modifying Database

* Update `pom.xml` for code generation database properties
* Update `service-demo/src/main/resources/application.yml` for demo database properties


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_

