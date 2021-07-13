# research-kotlin-springboot

**Template Multi-Module Kotlin Project**

Status        | Description
------------- | ------------- 
**STATUS**    | _Under Construction_
**STRATEGY**  | _Spike working example_

## Design

To create a template Kotlin project for showing boilerplate code.

* Use multi-modules to create 'starter' projects that encapsulate code and dependencies for common tech.
* Create demo SpringBoot application to pull everything together and provide working template

## Getting started

* `mvn clean install` 

### Prerequisites

* Docker, used by test-containers for testing 3rd party databases

### Modules

See module `README` files for specifics

Module        | Description
------------- | ------------- 
`research-kotlin-springboot-starter`    |  Common starter pack defining language, logging & testing dependencies
`research-kotlin-springboot-graphql`    |  Starter pack containing GraphQL dependencies and utilities
`research-kotlin-springboot-neo4j`      |  Starter pack containing Neo4j dependencies and utilities
`research-kotlin-springboot-bom`        |  Bill of Materials for all libraries in this collection
`research-kotlin-springboot-demo`       |  Demo SpringBoot Application


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


