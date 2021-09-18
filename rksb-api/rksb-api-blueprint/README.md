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


## User Guide

* Add new API Blueprint to `src/main/resources/api` folder (or subfolder)
* Add `<api-name>-blueprint.json`
* Add `<api-name>-create.sql` for creation of source table
* Finally, add the new blueprint file location into `blueprints.yml` to be included in code generation


## Developers Guide

* `mvn clean install` 

### Running the Generator from IDEA

GeneratorApplicationKt

```
--project.basedir=<repo-root-folder>/rksb-api/rksb-api-service
--project.version=0.1.0-SNAPSHOT
--app.codegen.base.package=me.roybailey.codegen
--app.datasource.main.url=jdbc:postgresql://localhost:5432/postgres
--app.datasource.main.username=postgres
--app.datasource.main.password=localhost
--app.datasource.jooq.url=jdbc:postgresql://localhost:5432/postgres
--app.datasource.jooq.username=postgres
--app.datasource.jooq.password=localhost
--app.datasource.jooq.schema=public
```

## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_

* TODO: update ApiBlueprintTest to optionally check for books

