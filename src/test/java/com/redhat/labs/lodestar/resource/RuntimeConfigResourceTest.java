package com.redhat.labs.lodestar.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.http.ContentType;
import io.smallrye.jwt.build.Jwt;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
class RuntimeConfigResourceTest {

    static final String RUNTIME_CONFIG_API = "/api/v1/configs/runtime";

    @Test
    void testGetRuntimeConfigurationNoType() {

        given().auth().oauth2(getValidAccessToken()).when().contentType(ContentType.JSON).get(RUNTIME_CONFIG_API).then().statusCode(200).body(is("\n{\n" + "    \"basic_information\": {\n" + "        \"engagement_types\": {\n"
                + "            \"options\": [\n" + "                {\n"
                + "                    \"label\": \"Type One\",\n" + "                    \"value\": \"TypeOne\"\n"
                + "                },\n" + "                {\n" + "                    \"label\": \"TypeTwo\",\n"
                + "                    \"value\": \"TypeTwo\"\n" + "                },\n" + "                {\n"
                + "                    \"label\": \"TypeThree\",\n" + "                    \"value\": \"TypeThree\",\n"
                + "                    \"default\": true\n" + "                }\n" + "            ]\n" + "        }\n"
                + "    },\n" + "    \"more_information\": {\n" + "        \"some_value\": \"hello\",\n"
                + "        \"another_value\": \"base\"\n" + "    },\n" + "    \"other_options\": {\n"
                + "        \"types\": {\n" + "            \"options\": [\n" + "                {\n"
                + "                    \"label\": \"OptionOne\",\n" + "                    \"value\": \"optionOne\"\n"
                + "                },\n" + "                {\n" + "                    \"label\": \"OptionTwo\",\n"
                + "                    \"value\": \"optionTwo\"\n" + "                }\n" + "            ]\n"
                + "        }\n" + "    }\n" + "}"));

    }

    @Test
    void testGetRuntimeConfigurationTypeOne() {

        given().auth().oauth2(getValidAccessToken()).when().contentType(ContentType.JSON).queryParam("type", "TypeOne").get(RUNTIME_CONFIG_API).then().statusCode(200).body(is("\n" + "{\n" + "    \"more_information\": {\n" + "        \"another_value\": \"type one\",\n"
                + "        \"some_value\": \"hello\"\n" + "    },\n" + "    \"basic_information\": {\n"
                + "        \"engagement_types\": {\n" + "            \"options\": [\n" + "                {\n"
                + "                    \"label\": \"Type One\",\n" + "                    \"value\": \"TypeOne\"\n"
                + "                },\n" + "                {\n" + "                    \"label\": \"TypeTwo\",\n"
                + "                    \"value\": \"TypeTwo\"\n" + "                },\n" + "                {\n"
                + "                    \"label\": \"TypeThree\",\n" + "                    \"value\": \"TypeThree\",\n"
                + "                    \"default\": true\n" + "                }\n" + "            ]\n" + "        }\n"
                + "    },\n" + "    \"other_options\": {\n" + "        \"types\": {\n" + "            \"options\": [\n"
                + "                {\n" + "                    \"label\": \"OptionOne\",\n"
                + "                    \"value\": \"option1\"\n" + "                }\n" + "            ]\n"
                + "        }\n" + "    }\n" + "}"));

    }

    @Test
    void testGetRuntimeConfigurationTypeTwo() {

        given().auth().oauth2(getValidAccessToken()).when().contentType(ContentType.JSON).queryParam("type", "TypeTwo").get(RUNTIME_CONFIG_API).then().statusCode(200).body(is("\n" + "{\n" + "    \"more_information\": {\n" + "        \"another_value\": \"type two\",\n"
                + "        \"some_value\": \"hello\"\n" + "    },\n" + "    \"basic_information\": {\n"
                + "        \"engagement_types\": {\n" + "            \"options\": [\n" + "                {\n"
                + "                    \"label\": \"Type One\",\n" + "                    \"value\": \"TypeOne\"\n"
                + "                },\n" + "                {\n" + "                    \"label\": \"TypeTwo\",\n"
                + "                    \"value\": \"TypeTwo\"\n" + "                },\n" + "                {\n"
                + "                    \"label\": \"TypeThree\",\n" + "                    \"value\": \"TypeThree\",\n"
                + "                    \"default\": true\n" + "                }\n" + "            ]\n" + "        }\n"
                + "    },\n" + "    \"other_options\": {\n" + "        \"types\": {\n" + "            \"options\": [\n"
                + "                {\n" + "                    \"label\": \"OptionTwo\",\n"
                + "                    \"value\": \"option2\"\n" + "                }\n" + "            ]\n"
                + "        }\n" + "    }\n" + "}"));

    }

    @Test
    void testGetRuntimeConfigurationTypeUnknown() {

        given().auth().oauth2(getValidAccessToken()).when().contentType(ContentType.JSON).queryParam("type", "TypeThree").get(RUNTIME_CONFIG_API).then().statusCode(200).body(is("\n" + "{\n" + "    \"more_information\": {\n" + "        \"some_value\": \"hello\",\n"
                + "        \"another_value\": \"base\"\n" + "    },\n" + "    \"basic_information\": {\n"
                + "        \"engagement_types\": {\n" + "            \"options\": [\n" + "                {\n"
                + "                    \"label\": \"Type One\",\n" + "                    \"value\": \"TypeOne\"\n"
                + "                },\n" + "                {\n" + "                    \"label\": \"TypeTwo\",\n"
                + "                    \"value\": \"TypeTwo\"\n" + "                },\n" + "                {\n"
                + "                    \"label\": \"TypeThree\",\n" + "                    \"value\": \"TypeThree\",\n"
                + "                    \"default\": true\n" + "                }\n" + "            ]\n" + "        }\n"
                + "    },\n" + "    \"other_options\": {\n" + "        \"types\": {\n" + "            \"options\": [\n"
                + "                {\n" + "                    \"label\": \"OptionOne\",\n"
                + "                    \"value\": \"optionOne\"\n" + "                },\n" + "                {\n"
                + "                    \"label\": \"OptionTwo\",\n" + "                    \"value\": \"optionTwo\"\n"
                + "                }\n" + "            ]\n" + "        }\n" + "    }\n" + "}"));

    }
    
    private String getValidAccessToken() {
        return Jwt.preferredUserName("alice")
                .groups(new HashSet<>(Arrays.asList("reader")))
                .issuer("https://quarkus.io/using-jwt-rbac")
                .audience("https://quarkus.io/using-jwt-rbac")
                .jws()
                .keyId("1")
                .sign();
    }

}
