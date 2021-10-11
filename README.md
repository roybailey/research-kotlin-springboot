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
[**`rksb-service`**](./rksb-service/README.md)  |  Deployable Services (inc. Manager for database schema migration)


## Getting Started

* `mvn clean install`
* `./demo.sh` to run the demo application (showcase common libraries)
* `./manager.sh` to run the manager application (showcase API services)

### Prerequisites

* **Java11**, this project contains 3rd party libraries that require Java11 or above 
* **Postgres** access for code generation, see [**`application-blueprint.yml`**](rksb-api/rksb-api-blueprint/src/main/resources/application-blueprint.yml) for properties
* **Docker**, optionally used by test-containers for testing 3rd party databases (excluded by default using JUnit5 tag `test-containers`) 

```
docker run --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=localhost postgres
```


### Adding API Blueprint Templates & Services

See [**`rksb-api/rksb-api-blueprint/README.md`**](rksb-api/rksb-api-blueprint/README.md) for instructions on adding a new API Blueprint Mapping Template
See [**`rksb-api/rksb-api-service/README.md`**](rksb-api/rksb-api-service/README.md) for instructions on using the API code generated services


### Adding 3rd Party Dependency

Versions of 3rd party dependencies used more than once such as logging, spring, utilities 
(i.e. not isolated to specialist library) are defined as a property in the main **`pom.xml`**

They are then declared using that version property in the **Bill-of-Materials** `pom.xml` for all other
modules and services to inherit.  This keeps the version definition in one place.

* Update [**`pom.xml`**](./pom.xml) with new dependency library version as a property
* Update [**Bill-of-Materials**](./rksb-common/rksb-common-bom/pom.xml) with library and version variable


### Adding New Common Library

Any specialist software should be confined to a common library module within 
[**Common Libraries**](./rksb-common/rksb-common-lib/pom.xml) `pom.xml`

* Add new module project to the [**Common Libraries**](./rksb-common/rksb-common-lib/) project
* Define required dependencies in the new libraries `pom.xml` 
* Add the new common library module to the [**Common Libraries**](./rksb-common/rksb-common-lib/pom.xml) `pom.xml`


### Database Configuration

* Update [**application-blueprint.yml**](./rksb-api/rksb-api-blueprint/src/main/resources/application-blueprint.yml) for updating database properties


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_

* TODO: Strategy for Loading/Caching meta data (e.g. total records, min/max dates etc.)

* DONE: Filter and column types as enum class values
* DONE: Camel case JSON object fields
* DONE: Generate basic filtering automatically using database columns
* DONE: Add date between two columns complex filter use-case
* DONE: Support overridable non-standard service methods and mappings from controller
* DONE: Cleanup generator and manager structure/configuration
* DONE: Generate AsciiDoc Blueprint report
* DONE: Support multiple endpoints for controllers
* DONE: Generate clean PoJos for API use
* DONE: Extend blueprint data classes to include all derived data needed for generators
* DONE: Create Manager catalogue of APIs
* DONE: Update Demo to test Flyway not required
* DONE: Map cleaner API path
* DONE: Add default limit
* DONE: Create Flyway Database Migration Manager
* DONE: Create Database Column Schema as part of Code Generation
* DONE: Create API Basic Filtering Config/Hooks
* DONE: Create API Controller Code Generator
* DONE: Create API Service Code Generator
* DONE: Create API Jooq Database Code Generator
* DONE: Create Demo application able to showcase code generated service
* DONE: Create API Code Generation module wired into Maven build
* DONE: Create Common Library module with BOM and Starter

