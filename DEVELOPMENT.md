# Development

**Table of contents:**

1. [Getting started](#getting-started)
1. [Build the project](#build-the-project)
1. [Generating step framework](#generating-step-framework)
1. [Best practices for writing steps](#best-practices-for-writing-steps)
1. [Testing](#testing)
1. [Debugging](#debugging)
1. [Release](#release)
1. [Pipeline Configuration](#pipeline-configuration)
1. [Security Setup](#security-setup)

## Getting started

1. [Ramp up your development environment](#ramp-up)
1. [Create and checkout a repo fork](#checkout-your-fork)
1. Optional: [Get Jenkins related environment](#jenkins-environment)
1. Optional: [Get familiar with Jenkins Pipelines as Code](#jenkins-pipelines)

### Ramp up

First you need to set up an appropriate development environment:

1. Install Pre-Commit
1. Install an IDE with Jenkins pipelines support, see for example [Pipeline in Visual Studio Code](https://www.jenkins.io/doc/book/pipeline/development/#visualstudio-code-jenkins-pipeline-linter-connector)

### Checkout your fork

To check out this repository:

1. Create your own
    [fork of this repo](https://help.github.com/articles/fork-a-repo/)
1. Clone it to your machine, for example like:

```shell
mkdir -p ${HOME}/projects
cd ${HOME}/projects
git clone git@github.com:${YOUR_GITHUB_USERNAME}/apm-pipeline-library.git
cd apm-pipeline-library
git remote add upstream git@github.com:elastic/apm-pipeline-library.git
```

### Jenkins environment

TBC

### Jenkins pipelines

The Jenkins related parts depend on

* [Jenkins Pipelines as Code](https://jenkins.io/doc/book/pipeline-as-code/)
* [Jenkins Shared Libraries](https://jenkins.io/doc/book/pipeline/shared-libraries/)

## Build the project

TBC

## Best practices for writing steps

TBC

## Testing

1. [Mocking](#mocking)
1. [Mockable Interface](#mockable-interface)
1. [Global function pointers](#global-function-pointers)
1. [Test Parallelization](#test-parallelization)

Unit tests are done using basic `golang` means.


### Mocking

Tests should be written only for the code of your step implementation, while any
external functionality should be mocked, in order to test all code paths including
the error cases.


## Debugging

Debugging can be initiated with VS code fairly easily. Compile the binary with specific compiler flags to turn off optimizations `go build -gcflags "all=-N -l" -o piper.exe`.

Modify the `launch.json` located in folder `.vscode` of your project root to point with `program` exactly to the binary that you just built with above command - must be an absolute path. Add any arguments required for the execution of the Piper step to `args`. What is separated with a blank on the command line must go into a separate string.

```javascript
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "name": "Launch",
            "type": "go",
            "request": "launch",
            "mode": "exec",
            "program": "C:/CF@HCP/git/jenkins-library-public/piper.exe",
            "env": {},
            "args": ["checkmarxExecuteScan", "--password", "abcd", "--username", "1234", "--projectName", "testProject4711", "--serverUrl", "https://cx.server.com/"]
        }
    ]
}
```

Finally, set your breakpoints and use the `Launch` button in the VS code UI to start debugging.

## Release

Releases are performed using [Project "Piper" Action](https://github.com/SAP/project-piper-action).
We release on schedule (once a week) and on demand.
To perform a release, the respective action must be invoked for which a convenience script is available in `contrib/perform-release.sh`.
It requires a personal access token for GitHub with `repo` scope.
Example usage `PIPER_RELEASE_TOKEN=THIS_IS_MY_TOKEN contrib/perform-release.sh`.

## Pipeline Configuration

The pipeline configuration is organized in a hierarchical manner and configuration parameters are incorporated from multiple sources.
In general, there are four sources for configurations:

1. Directly passed step parameters
1. Project specific configuration placed in `.pipeline/config.yml`
1. Custom default configuration provided in `customDefaults` parameter of the project config or passed as parameter to the step `setupCommonPipelineEnvironment`
1. Default configuration from Piper library

For more information and examples on how to configure a project, please refer to the [configuration documentation](https://sap.github.io/jenkins-library/configuration/).

### Groovy vs. Go step configuration

The configuration of a project is, as of now, resolved separately for Groovy and Go steps.
There are, however, dependencies between the steps responsible for resolving the configuration.
The following provides an overview of the central components and their dependencies.

#### setupCommonPipelineEnvironment (Groovy)

The step `setupCommonPipelineEnvironment` initializes the `commonPipelineEnvironment` and `DefaultValueCache`.
Custom default configurations can be provided as parameters to `setupCommonPipelineEnvironment` or via the `customDefaults` parameter in project configuration.

#### DefaultValueCache (Groovy)

The `DefaultValueCache` caches the resolved (custom) default pipeline configuration and the list of configurations that contributed to the result.
On initialization, it merges the provided custom default configurations with the default configuration from Piper library, as per the hierarchical order.

Note, the list of configurations cached by `DefaultValueCache` is used to pass path to the (custom) default configurations of each Go step.
It only contains the paths of configurations which are **not** provided via `customDefaults` parameter of the project configuration, since the Go layer already resolves configurations provided via `customDefaults` parameter independently.

## Additional Developer Hints

You can find additional hints at [documentation/developer-hints](./documentation/developer_hints)
