package com.redhat.labs.lodestar.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class HealthCheckResourceTest {

    @Test
    void testGetHealth() {

        String expected = "\n{\n" + "    \"status\": \"UP\",\n" + "    \"checks\": [\n" + "        {\n"
                + "            \"name\": \"👍\",\n" + "            \"status\": \"UP\"\n" + "        },\n"
                + "        {\n" + "            \"name\": \"Runtime Config Available\",\n"
                + "            \"status\": \"UP\",\n" + "            \"data\": {\n" + "                \"OK\": \"👍\"\n"
                + "            }\n" + "        }\n" + "    ]\n" + "}";

        given().when().contentType(ContentType.JSON).get("q/health").then().statusCode(200).body(is(expected));

    }

}
