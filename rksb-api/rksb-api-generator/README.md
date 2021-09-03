# API Generator

**Module containing API code generation utilities**


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

### Database Code Generation

This requires database access to be defined in the `application.yml`, or overridden in the program arguments.

### API Specification File

An api specification file should be created in the `../docs` folder named `<domain>-spec.json`.
This will be parsed and used to generator code.
e.g.

```
{
  "name": "books",
  "tableMapping": [
    {
      "table": "TEMP_BOOKS",
      "record": "TempBooksRecord",
      "domain": "TempBooks",
      "columnMapping": [],
      "testData": []
    }
  ]
}
```

### Database Table Create SQL File

A table create sql should be added to the same folder named `<domain>-create.sql`.
This will be parsed to capture column names and data types.
e.g.

```
drop table if exists temp_books;

create table temp_books
(
    id              serial not null
        constraint temp_books_pkey
            primary key,
    title           varchar(255),
    description     text,
    publicationdate integer,
    price     double precision
);

drop view if exists v_temp_books;
create view v_temp_books as select * from temp_books;
```


## Build & Test

* `mvn clean install` 


### Prerequisites

* Local Postgres instance (running using Docker defaults)


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_

* Enrich ApiBlueprint object graph to reduce custom model entries for code generation
