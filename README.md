# research-kotlin-springboot

**Template Multi-Module Kotlin Project**

Status        | Description
------------- | ------------- 
**STATUS**    | _Under Construction_
**STRATEGY**  | _Spike working example_

## Design

To create a template Kotlin project for showing boilerplate code.

* Use multi-modules to create common libraries and 'starter' projects that encapsulate code and dependencies
* Create demo SpringBoot application to pull everything together and provide working template

## Getting started

* `mvn clean install` 

### Prerequisites

* Docker, used by test-containers for testing 3rd party databases

### Modules

See module `README` files for specifics

Module          | Description
--------------- | ------------- 
`rksb-common`   |  Common libraries and starter packs defining language, logging, testing and 3rd party dependencies
`rksb-api`      |  API definition and code generation
`rksb-service`  |  Service examples


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


