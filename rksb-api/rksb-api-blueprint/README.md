# API Blueprint

**Module containing API Blueprint Definitions**

Module of [**`..`**](../README.md) *parent*


## Design

Modules to encapsulate all api blueprint definitions.
These blueprints provide the following...

* Database Table details
* API details

These are stored in the `resources` folder so they can be used both by the build system
for code generation and loaded at runtime for common code logic.

For example:  The build system can code generate a `RestController` while the runtime
might use the database column lists to handle common column logic.

`ApiBlueprintConfiguration` contains the SpringBoot beans that load the blueprints and merge with database schema


## User Guide

* Add new API Blueprint to `src/main/resources/api` folder (or subfolder)
* Add `<api-name>-blueprint.json`
* Finally, add the new blueprint file location into `application-blueprints.yml` to be included in code generation

```
api.blueprints:
  - api/books-blueprint.json
  - api/cities-blueprint.json
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
