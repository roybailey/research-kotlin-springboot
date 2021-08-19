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


## Getting started

* `mvn clean install` 

### Prerequisites

* Docker, used by test-containers for testing 3rd party databases (or uncomment `<excludes>**TestContainer**</exclude>` from `pom.xml` to avoid)
* Postgres access for code generation

```
docker run --name postgres -e POSTGRES_PASSWORD=localhost postgres
```

## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


