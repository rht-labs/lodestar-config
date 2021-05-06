![Build Container](https://github.com/rht-labs/lodestar-config/workflows/Build%20Container/badge.svg)

# Lodestar - Config

The Configuration API for Lodestar.

----

# JSON REST API

## OpenAPI Documentation of APIs

The JSON APIs are documented using using OpenAPI.  The OpenAPI UI can be used to view the exposed endpoints and interact directly with them.

Once the application is launched, the OpenAPI UI can be found at the following path:

```
http(s)://your-hostname[:port]/q/swagger-ui
```

## Available Resources

### RuntimeConfig

The runtime config resource exposes an endpoints that allow clients to retrieve the base runtime configuration or the base configuration merged with a specific engagement type configuration.

`GET /engagements`

The following parameters are supported:

Query Params:
  * `type` - engagement type value listed in the base configuration file.

----

## Configuration

The following environment variables are available:

### Logging

| Name | Example Value | Required |
|------|---------------|----------|
| LODESTAR_CONFIG_LOGGING | INFO | False |
| LODESTAR_CONFIG_MIN_LOGGING | TRACE | false |

### Runtime Configuration

| Name | Example Value | Required |
|------|---------------|----------|
| RUNTIME_BASE_CONFIG_FILE | /runtime/lodestar-runtime-configuration.yaml | False |

## Development

See [the development README](deployment/README.md) for details on how to spin up a deployment for developing on OpenShift.

## Components

This project was built using Quarkus.

## Testing

`./mvw test` can be used to run the unit tests for the application.

## Useful Commands

``` bash
# serve with hot reload at localhost:8080
mvn quarkus:dev
# run unit tests
mvn test
# build for production
mvn quarkus:build
```
