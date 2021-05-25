package com.redhat.labs.lodestar.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyOrderStrategy;

import org.junit.jupiter.api.Test;

class ConfigServiceTest {

    JsonbConfig config = new JsonbConfig().withFormatting(true).withPropertyOrderStrategy(PropertyOrderStrategy.LEXICOGRAPHICAL);
    Jsonb jsonb = JsonbBuilder.create(config);

    @Test
    void testGetBaseConfiguration() {

        String expected = "\n{\n" + "    \"basic_information\": {\n" + "        \"engagement_types\": {\n"
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
                + "        }\n" + "    }\n" + "}";

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/base-config.yaml";

        service.createRuntimeConfigurations();

        String configuration = service.getRuntimeConfiguration(Optional.empty());
        assertEquals(expected, configuration);

    }

    @Test
    void testGetTypeOneConfiguration() {

        String expected = "\n" + "{\n" + "    \"more_information\": {\n" + "        \"another_value\": \"type one\",\n"
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
                + "        }\n" + "    }\n" + "}";

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/base-config.yaml";

        service.createRuntimeConfigurations();

        String configuration = service.getRuntimeConfiguration(Optional.of("TypeOne"));
        assertEquals(expected, configuration);

    }

    @Test
    void testGetTypeTwoConfiguration() {

        String expected = "\n" + "{\n" + "    \"more_information\": {\n" + "        \"another_value\": \"type two\",\n"
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
                + "        }\n" + "    }\n" + "}";

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/base-config.yaml";

        service.createRuntimeConfigurations();

        String configuration = service.getRuntimeConfiguration(Optional.of("TypeTwo"));
        assertEquals(expected, configuration);

    }

    @Test
    void testGetTypeThreeConfigurationNotFound() {

        String expected = "\n" + "{\n" + "    \"more_information\": {\n" + "        \"some_value\": \"hello\",\n"
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
                + "                }\n" + "            ]\n" + "        }\n" + "    }\n" + "}";

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/base-config.yaml";

        service.createRuntimeConfigurations();

        String configuration = service.getRuntimeConfiguration(Optional.of("TypeThree"));
        assertEquals(expected, configuration);

    }

}
