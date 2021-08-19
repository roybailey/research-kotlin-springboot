# Common

**Common Libraries Modules**

## Design

Modules intended to generate boilerplate code through layering of code generation steps as follows

1. Build the api domain configurations (e.g. definition of all requirements and configurations for code generation)
1. Run domain process to update the database schema
1. Generate typesafe Java database schema using jooq (requires database access and any schema changes e.g. views) 
1. Generate 


## Modules

Module          | Description
--------------- | ------------- 
[**`rksb-common-lib`**](./rksb-common-lib/README.md)    |  Common library modules for language and 3rd party 
[**`rksb-common-bom`**](./rksb-common-bom/README.md)    |  Book of Materials, versioning for all 3rd party / common libraries


## Getting started

* `mvn clean install` 


### Prerequisites

* Docker, used by test-containers for testing 3rd party databases


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


