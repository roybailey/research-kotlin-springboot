# Common BOM

**Common Book of Materials for defining all versioning of 3rd party and common libraries**

## Design

`pom.xml` to define all versioning for common 3rd party and custom libraries.

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

## Getting started

* `mvn clean install` 


## Handover Suggestions

_Nuggets of Knowledge and Thinking from last people to work on the project._
_e.g. suggestions for technical debt reduction, simplification or enhancements_


