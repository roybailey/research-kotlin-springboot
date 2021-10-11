# API Generator

**Module containing API code generation utilities**

Module of [**`..`**](../README.md) *parent*


## Design

An application to compile blueprint templates and code-generate from database schema.

The intention is to generate boiler-plate code from both database and an API blueprints.
Much of the vanilla capability of an API service can be generated
or at least enough around the column names and stubs needed to reduce the amount and complexity, custom code, and typos.

The following frameworks are used to perform database and code generation respectively.

* [jooq.org](https://jooq.org) is used to generate from database schema
* [handlebars.java](https://github.com/jknack/handlebars.java) is used to generate source code

Code generation should be kept as simplistic as possible, the minimum needed to drive the common code library.

e.g. generating classes with simple stubs and common base code calls, 
allowing the common code libraries to handle the more complex code itself.


## Getting Started

* Run the `GeneratorApplication` and pass `--project.basedir=<path to service module>/rksb-api-service --project.version=0.0.0-SNAPSHOT`

This uses the database source and blueprint templates defined in the `application-blueprint.yml`
from the `rksb-api-blueprint` module.  And also applies the database schema migration from the `rksb-api-blueprint` module.  


## Build & Test

* `mvn clean install` 


### Prerequisites

* Local Postgres instance (running using Docker defaults)


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_

