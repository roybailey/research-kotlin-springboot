# Research Kotlin SpringBoot - Common Bill of Materials

**Common Bill of Materials for defining all versioning of 3rd party and common libraries**

Module of [**`..`**](../README.md) *parent*


## Design

* `pom.xml` to define all versioning for common 3rd party and custom libraries.
* Versions should mostly be defined in the root `pom.xml` unless very specialist


### Usage

Add to your `pom.xml` the following using the appropriate version.

```
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>me.roybailey</groupId>
				<artifactId>rksb-common-bom</artifactId>
				<version>${project.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
```

Then add any dependecies you require without need for version as this will come from the `bom`

```
    <dependency>
        <groupId>me.roybailey</groupId>
        <artifactId>rksb-common-lib-base</artifactId>
    </dependency>
```


## Getting started

* `mvn clean install` 


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


