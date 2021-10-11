# API Generator

**Module containing API code generation utilities**

Module of [**`..`**](../README.md) *parent*


## Design

An application that can be wired into a maven build to code-generate from database schema and API definitions.

The intention is to generate boiler-plate code from both database and an API specification.
Much of the vanilla capability of an API service can be generated
or at least enough around the column names and stubs needed to reduce the amount and complexity of custom code.

* [jooq.org](https://jooq.org) is used to generate from database schema
* [handlebars.java](https://github.com/jknack/handlebars.java) is used to generate source code

Code generation should be kept as simplistic as possible, the minimum needed to drive common code.
e.g. generating classes with String identifiers and let the common base code lookup the real objects
(rather than generating more complex code to lookup real objects directly).


## Getting Started

* Run the `GeneratorApplication` and pass `--project.basedir=.../rksb-api-service --project.version=0.0.0-SNAPSHOT`

This uses the database source and blueprint templates defined in the `application-blueprint.yml`
from the `rksb-api-blueprint` module.  And also applies the database schema migration from the `rksb-api-blueprint` module.  


## Build & Test

* `mvn clean install` 


### Prerequisites

* Local Postgres instance (running using Docker defaults)


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_

