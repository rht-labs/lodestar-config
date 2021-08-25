package com.redhat.labs.lodestar.resource;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@TestHTTPEndpoint(GitlabHookConfigResource.class)
class WebhookConfigResourceTest {

    @Test
   void testGet() {
       RestAssured.given().when().get().then().statusCode(200).header("x-total-webhooks", equalTo("3")).body("size()", equalTo(3));
   }
}
