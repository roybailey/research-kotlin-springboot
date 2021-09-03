# API Blueprint

**Module containing API Blueprint Definitions**


## Design

Modules to encapsulate all api blueprint definitions.
These blueprints provide the following...

* Source Table details
* SQL Create DDL definitions

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


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_

* TODO: update ApiBlueprintTest to optionally check for books
* TODO: update ApiBlueprintTest to read the `blueprint.yml` and check count

