# API

**Module containing API related definitions, common and code generated libraries**


## Design

Modules intended to generate boilerplate code through layering of code generation steps as follows

1. Build the api domain configurations (e.g. definition of all requirements and configurations for code generation)
1. Run generator process to update the database schema and generate typesafe Java database schema


## Modules

Module          | Description
--------------- | ------------- 
[**`rksb-api-blueprint`**](./rksb-api-blueprint/README.md)    |  API blueprints, definitions and documentation
[**`rksb-api-common`**](./rksb-api-common/README.md)          |  API common custom code library and blueprint loader
[**`rksb-api-generator`**](./rksb-api-generator/README.md)    |  API database and code generation tooling
[**`rksb-api-service`**](./rksb-api-service/README.md)        |  API combined common and generated code library


## Getting started

* `mvn clean install` 

### Prerequisites

* Local Postgres instance (running using Docker defaults)


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


