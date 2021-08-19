# Library

**Library Code Modules**


## Design

Libraries built together in the same project version.

* Use multi-modules to create common libraries projects that encapsulate code and dependencies
* Each library should
  * Define 3rd party dependencies
  * Create utilities and wrappers to simplify setup/usage
  * Provide good `README` instructions
  * Include testing to verify dependencies and utilities work 


## Modules

Module          | Description
--------------- | ------------- 
[**`rksb-common-lib-base`**](./rksb-common-lib-base/README.md)          |  Base library for Language & Logging dependencies/utilities
[**`rksb-common-lib-database`**](./rksb-common-lib-database/README.md)  |  Database library for Postgres dependencies/utilities
[**`rksb-common-lib-web`**](./rksb-common-lib-web/README.md)            |  Web/RESTful library for Web Service dependencies/utilities
[**`rksb-common-lib-test`**](./rksb-common-lib-test/README.md)          |  Test library for Testing dependencies/utilities
[**`rksb-common-lib-neo4j`**](./rksb-common-lib-neo4j/README.md)        |  Neo4j graph database dependencies/utilities
[**`rksb-common-lib-graphql`**](./rksb-common-lib-graphql/README.md)    |  GraphQL dependencies/utilities


## Getting started

* `mvn clean install` 

### Prerequisites

* Docker, used by test-containers for testing 3rd party databases


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


