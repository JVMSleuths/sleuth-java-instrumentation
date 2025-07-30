# sleuth-java-instrumentation

A JVM runtime instrumentation tool for detecting unused code in Java applications.

## Goal

Confidently identify unused code by leveraging JVM runtime data to report which classes are available. Complement this data with compile-time data sources to identify unused classes.

## Overview

`sleuth-java-instrumentation` is a Java agent that helps developers identify dead code in their applications by combining runtime class loading information with static analysis. This dual approach provides more accurate detection of truly unused code compared to static analysis alone.

# Working In Progress
below are aspirational feature and goals of this project.  

## Features

- **Runtime Class Loading Detection**: Monitors which classes are actually loaded and used during application execution
- **JVM Agent Architecture**: Non-intrusive instrumentation that doesn't affect application performance
- **Accurate Dead Code Identification**: Reduces false positives by using actual runtime behavior
- **Integration Ready**: Designed to work with existing build tools and CI/CD pipelines

## How It Works

1. **Instrumentation Phase**: The Java agent instruments class loading to track which classes are accessed during runtime
2. **Runtime Monitoring**: Collects data about class usage patterns during application execution
4. **Report Generation**: Combines runtime and compile-time data to generate reports of unused classes

## Installation

### As a Java Agent

```bash
java -javaagent:sleuth-java-instrumentation.jar -jar your-application.jar
```



## Usage

### Basic Usage

1. Run your application with the sleuth agent attached
2. Exercise your application's functionality (run tests, manual testing, etc.)
3. Generate the unused code report




## Use Cases to consider

- **Code Cleanup**: Identify and remove dead code before releases
- **Dependency Analysis**: Find unused dependencies and reduce JAR size
- **Refactoring**: Safely remove code during large refactoring efforts
- **CI/CD Integration**: Automatically detect code drift in continuous integration
- **Legacy System Modernization**: Understand which parts of legacy systems are still active

## Requirements

- Java 8 or higher
- Compatible with all major JVM implementations (HotSpot, OpenJ9, GraalVM)
- Works with Spring Boot, Jakarta EE, and standalone Java applications




## Support

- **Issues**: Report bugs and feature requests on [GitHub Issues](https://github.com/JVMSleuths/sleuth-java-instrumentation/issues)
- **Discussions**: Join the conversation in [GitHub Discussions](https://github.com/JVMSleuths/sleuth-java-instrumentation/discussions)



Made with ❤️ by the JVMSleuths team