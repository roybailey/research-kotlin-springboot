# API Blueprint

**Module containing API Blueprint Templates**

Module of [**`..`**](../README.md) *parent*


## Design

Module to encapsulate all blueprint templates.
These blueprints provide the following...

* Controller API Endpoints
* DAO Services with Filtering
* Database Schema Changes
* Database Table Details
* Data Models for loading from Database and serving as JSON

These are stored in the `resources` folder so that they can be used both by the build system
for code generation and loaded at runtime for common code logic.

For example:  The build system can code generate a `RestController` while the runtime
might use the database column lists to provide generic column logic.

### Blueprint Database Schema Migration

This module contains the Flyway Database Migration schema DDLs and code to apply to a target database.

It uses a custom migration table `blueprint-schema-history` and is enabled via a property `blueprints.flyway.enabled=true`

Only called by the code generator (in DEV to ensure code is generated against the target database state)
and by the manager service to apply the schema migration through all environments (UAT and PROD)

NOTE: the schema files should be idempotent, meaning you can destroy the `blueprint-schema-history` table and
re-run all the schema changes to arrive at the correct schema state without errors.
e.g. use DROP IF EXISTS, then CREATE

## User Guide

* `src/main/resources/api` folder, add new Blueprint template file
  * e.g. `<api-name>-blueprint.json` file, create and populate accordingly
* `src/main/resources/ddl` folder, add new Database schema view
  * e.g. `V0003__<api-name>.sql` file and populate accordingly (using next free sequence number)
* `application-blueprint.yml`, add `- api/<api-name>-blueprint.json` to `blueprint-templates` list 

```
blueprint-templates:
  - api/codegen-sample-blueprint.json
  - api/<api-name>-blueprint.json
```

### Blueprint Specification

See `Blueprint.kt` source for full data class definition

See `codegen-sample-blueprint.json` for example of blueprint definition (this is used to test the code generation process)

#### Controller Mappings

ControllerMappings define a RESTful controller class for linking API calls to Service DAO methods.

```
```

#### Service Mappings

ServiceMappings define a DAO class for loading from the database into an object model.

```
```

### TableMappings

TableMappings define a database table and is used to generate table/column details directly from the database.

```
```

#### Model Mappings

ModelMappings define a POJO data class for loading from the database and serving via Services/Controllers.

```
```


## Developers Guide

* `mvn clean install` 

### Running the Generator from IDEA

GeneratorApplicationKt

```
--project.basedir=<repo-root-folder>/rksb-api/rksb-api-service
--project.version=0.1.0-SNAPSHOT
```

## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_

* Care must be taken with dependencies in this module as it is used by the generator in a build process
  and by the manager for database migration and for services to load and support an API
