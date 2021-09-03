# research-kotlin-springboot

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

* Postgres access for code generation, see `pom.xml` for properties to assign
* Docker, used by test-containers for testing 3rd party databases (excluded by default using JUnit5 tag `test-containers`) 

```
docker run --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=localhost postgres
```

## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


