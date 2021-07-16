# research-kotlin-springboot-neo4j

**Library for including Neo4j embedded within a service**

Status        | Description
------------- | ------------- 
**STATUS**    | _Under Construction_
**STRATEGY**  | _Spike working example_

## Design

Built with Neo4j v4.0.x (latest recommended and working combination)

* Bring together Neo4j embedded database with Java driver and APOC procedures
* Provide common utilities to make data import easier

The main purpose is wrap the Neo4j database into a service that handles the
setup as either embedded or server based using the Bolt driver.

There is a common `Neo4jService` which gets instantiated based on the `neo4j.uri`
as either a `Neo4jBoltService` or `Neo4jEmbeddedService`

The primary purpose is to allow embedded graph database use for in-memory/caching
solutions in API services.  However, the design allows you to change to server based
graph database instance with just a config change.

Note:  If you create an embedded neo4j database but do not purge the database on startup
then (assuming the same file location) the previous graph data will be retained.
This makes it a nice solution for file backed caching of data. 

## Building & Running Locally

* `mvn clean install`

### Prerequisites

* Docker, used by test-containers for testing 3rd party databases
* Local Neo4j install for accessing via Neo4j browser

## Getting started

### Key Configuration

Package a `neo4j.conf` in your project (use the one in this project under `test/resources` as template)

Your SpringBoot application.yml will need to specify the neo4j service settings
```yaml
neo4j:
  uri: file://./target/neo4j/graph.db
  username: neo4j
  password: localhost
  reset: purge
  bolt:
    connector:
      port: 7887
```

Module                       | Description
---------------------------- | ------------- 
`neo4j.reset`                | `purge` will delete all data upon database startup (use when you intend to rehydrate)
`neo4j.bolt.connector.port`  | Will be the port you can access the database from Neo4j Browser


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


