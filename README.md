![Build Container](https://github.com/rht-labs/lodestar-config/workflows/Build%20Container/badge.svg)

# Lodestar - Config

The Configuration API for Lodestar.

## Runtime Configurations

The base runtime configuration is a Yaml file.  The only required contents for the base configuration is the following:

```
basic_information:
  engagement_types:
    options:
      - label: Type One
        value: TypeOne
      - label: TypeTwo
        value: TypeTwo
```

The options list in the `engagement_types` will be used to determine if there are any override files to merge. Base config values can be added to or overriden using override files with the naming scheme `<base-file-name>-<engagement_type_value>.yaml`.

For example:

If the base configuration name is `runtime-config.yaml` and you wanted to add to or override values for engagement type TypeTwo, you would need to create a mounted file with the name `runtime-config-typetwo.yaml`.

If no file found matching the value of the engagement type, the base config will be returned.

### Runtime Configuration Merging

- All entries in the base config and not in the override config will be included
- All entries in the override config and not in the base config will be included
- All entries, excluding lists, that are in both the base and override configuration will use the values in the override config
- **Any list that is in both base and override configs will be replaced with the values in the override config**

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

`GET /api/v1/configs/runtime`

The following parameters are supported:

Query Params:
  * `type` - engagement type value listed in the base configuration file. If not found, it reverts to the base

### Rbac Perimissions

This resource returns a map of engagement types with a list of roles (groups) that have writer permission

`GET /api/v1/configs/runtime/rbac`

### Webhooks Needed by engagements

Provides a list of webhooks that need to be added to each engagement on creation.

----

## Configuration

The following environment variables are available:

### Logging

| Name | Example Value | Required |
|------|---------------|----------|
| LODESTAR_CONFIG_LOGGING | INFO | False |
| LODESTAR_CONFIG_MIN_LOGGING | TRACE | false |

### Runtime Configuration

| Name | Default Value | Required |
|------|---------------|----------|
| RUNTIME_BASE_CONFIG_FILE | /runtime/base/lodestar-runtime-config-base.yaml | False |
| WEBHOOK_BASE_CONFIG_FILE | /webhooks/webhooks.yaml | False |
| LODESTAR_ENGAGEMENTS_API_URL | http://lodestar-engagements:8080 | False

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
