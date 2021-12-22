# Change Log

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]
### Added

* TODO: Migrate to renamed mono-repo structure, common, api, support, manager
* TODO: Add TestContainers to provide unit testable code generation
* TODO: Add Docker and helmchart build plugins
* TODO: Docker image with Java and Neo4j
* TODO: Strategy for Loading/Caching meta data (e.g. total records, min/max dates etc.)

### Changed

## 2021
### Added

* DONE: Filter and column types as enum class values
* DONE: Camel case JSON object fields
* DONE: Generate basic filtering automatically using database columns
* DONE: Add date between two columns complex filter use-case
* DONE: Support overridable non-standard service methods and mappings from controller
* DONE: Cleanup generator and manager structure/configuration
* DONE: Generate AsciiDoc Blueprint report
* DONE: Support multiple endpoints for controllers
* DONE: Generate clean PoJos for API use
* DONE: Extend blueprint data classes to include all derived data needed for generators
* DONE: Create Manager catalogue of APIs
* DONE: Update Demo to test Flyway not required
* DONE: Map cleaner API path
* DONE: Add default limit
* DONE: Create Flyway Database Migration Manager
* DONE: Create Database Column Schema as part of Code Generation
* DONE: Create API Basic Filtering Config/Hooks
* DONE: Create API Controller Code Generator
* DONE: Create API Service Code Generator
* DONE: Create API Jooq Database Code Generator
* DONE: Create Demo application able to showcase code generated service
* DONE: Create API Code Generation module wired into Maven build
* DONE: Create Common Library module with BOM and Starter
