# API

**Module containing API related definitions, common and code generated libraries**

**STATUS** _Under Construction_


## Design

Modules intended to generate boilerplate code through layering of code generation steps as follows

1. Build the api domain configurations (e.g. definition of all requirements and configurations for code generation)
1. Run generator process to update the database schema and generate typesafe Java database schema


## Modules

Module          | Description
--------------- | ------------- 
[**`docs`**](./docs/README.md)                                |  API documentation and definition 
[**`rksb-api-generator`**](./rksb-api-generator/README.md)    |  API database and code generation tooling
[**`rksb-api-domain`**](./rksb-api-domain/README.md)          |  API domain of generated and common code library


## Getting started

* `mvn clean install` 

### Prerequisites

* Local Postgres instance (running using Docker defaults)


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


