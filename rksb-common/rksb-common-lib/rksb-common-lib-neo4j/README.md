# research-kotlin-springboot-neo4j

**Library for including Neo4j embedded within a service**

Status        | Description
------------- | ------------- 
**STATUS**    | _Under Construction_
**STRATEGY**  | _Spike working example_

## Design

* Bring together Neo4j embedded database with Java driver and APOC procedures
* Provide common utilities to make data import easier

## Getting started

* `mvn clean install` 

### Prerequisites

* Docker, used by test-containers for testing 3rd party databases

### Key Configuration

Configuration | Description
------------- | ------------- 
`research-kotlin-springboot-starter`    |  Common starter pack defining language, logging & testing dependencies
`research-kotlin-springboot-graphql`    |  Starter pack containing GraphQL dependencies and utilities
`research-kotlin-springboot-neo4j`      |  Starter pack containing Neo4j dependencies and utilities
`research-kotlin-springboot-bom`        |  Bill of Materials for all libraries in this collection
`research-kotlin-springboot-demo`       |  Demo SpringBoot Application


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


