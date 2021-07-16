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

## Getting started

* `mvn clean install` 

### Prerequisites

* Docker, used by test-containers for testing 3rd party databases

### Modules

See module `README` files for specifics

Module           | Description
---------------- | ------------- 
`rksb-base`      | Base library for Language & Logging dependencies/utilities
`rksb-test`      | Test library for Testing dependencies/utilities
`rksb-neo4j`     | Neo4j graph database dependencies/utilities
`rksb-graphql`   | GraphQL dependencies/utilities


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


