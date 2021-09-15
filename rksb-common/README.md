# Research Kotlin SpringBoot - Common

**Common Libraries Modules**

## Design

Modules of common code built together and versioned within a bom (bill-of-materials) 

1. Module to contain all common libraries
1. A bill-of-materials module to version all 3rd party and common libraries
1. Various 'starter' projects for common dependencies 


## Modules

Module          | Description
--------------- | ------------- 
[**`..`**](../README.md) | *parent*
[**`rksb-common-lib`**](./rksb-common-lib/README.md)            |  Common library modules for language and 3rd party 
[**`rksb-common-bom`**](./rksb-common-bom/README.md)            |  Book of Materials, versioning for all 3rd party / common libraries
[**`rksb-common-starter`**](./rksb-common-starter/README.md)              |  Starter project containing standard REST/database dependencies
[**`rksb-common-starter-test`**](./rksb-common-starter-test/README.md)    |  Starter project containing standard Test dependencies


## Getting started

* `mvn clean install` 


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


