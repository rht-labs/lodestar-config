package com.redhat.labs.lodestar.resource;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.redhat.labs.lodestar.utils.ResourceLoader;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestHTTPEndpoint(RuntimeConfigResource.class)
class RuntimeConfigResourceTest {
    
    @Test
    void testGetPermission() {
        String response = given().when().get("/rbac").then().statusCode(200).extract().asString();
        
        Map<String, List<String>> perm = from(response).getMap("");

        assertTrue(perm.containsKey("velour"));
        assertTrue(perm.containsKey("velvet"));
        assertTrue(perm.containsKey("felt"));
        assertTrue(perm.get("velour").contains("writer"));
        assertTrue(perm.get("velvet").contains("writer"));
        assertTrue(perm.get("felt").contains("writer"));
        assertTrue(perm.get("velour").contains("velour-writer"));
        assertTrue(perm.get("felt").contains("felt-writer"));
        assertEquals(3, perm.size());
        assertEquals(2, perm.get("velour").size());
        assertEquals(2, perm.get("felt").size());
        assertEquals(1, perm.get("velvet").size());
    }
    
    @Test
    void testGetRuntimeConfigurationNoType() {
        String expectedResponse = ResourceLoader.load("expected/service-base-config.json");
        String response = given().when().contentType(ContentType.JSON).get()
                .then().statusCode(200).extract().asString();

        assertEquals(expectedResponse, response);

    }

    @Test
    void testGetRuntimeConfigurationTypeOne() throws Exception {
        String expectedResponse = ResourceLoader.load("expected/service-get-type-one-config.json");
        String response = given().when().contentType(ContentType.JSON).queryParam("type", "TypeOne").get()
                .then().statusCode(200).extract().asString();

        assertEquals(expectedResponse, response);

    }

    @Test
    void testGetRuntimeConfigurationTypeTwo() {
        String expectedResponse = ResourceLoader.load("expected/service-get-type-two-config.json");
        String response = given().when().contentType(ContentType.JSON).queryParam("type", "TypeTwo").get()
                .then().statusCode(200).extract().asString();

        assertEquals(expectedResponse, response);

    }

    @Test
    void testGetRuntimeConfigurationTypeUnknown() {
        String response = ResourceLoader.load("expected/service-base-config.json");
        given().when().contentType(ContentType.JSON).queryParam("type", "TypeThree").get().then().statusCode(200).body(is(response));

    }

    @Test
    void testGetArtifactOptions() {
        given().when().contentType(ContentType.JSON).get("artifact/options")
                .then().statusCode(200).body("size()", equalTo(1))
                .body("flyer", equalTo("Flyer"));

    }

    @Test
    void testGetEngagementRegionOptions() {
        given().when().contentType(ContentType.JSON).get("region/options")
                .then().statusCode(200).body("size()", equalTo(1))
                .body("tuscana", equalTo("Tuscony"));

    }

    @Test
    void testGetEngagementOptions() {
        given().when().contentType(ContentType.JSON).get("engagement/options")
                .then().statusCode(200).body("size()", equalTo(3))
                .body("TypeOne", equalTo("Type One"));

    }

    @Test
    void testGetParticipantOptions() {
        given().when().contentType(ContentType.JSON).get("participant/options")
                .then().statusCode(200).body("size()", equalTo(2))
                .body("jurist", equalTo("Jurist")).body("judge", equalTo("Judge"));

    }

    @Test
    void testGetParticipantOptionsTyped() {
        given().queryParam("engagementType", "TypeTwo").when().contentType(ContentType.JSON).get("participant/options")
                .then().statusCode(200).body("size()", equalTo(2))
                .body("plaintiff", equalTo("Plaintiff")).body("defendant", equalTo("Defendant"));

    }

}
