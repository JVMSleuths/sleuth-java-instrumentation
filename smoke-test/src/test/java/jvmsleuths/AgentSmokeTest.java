package jvmsleuths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;


import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AgentSmokeTest {

    @Container
    private static final GenericContainer<?> app =
            new GenericContainer<>(DockerImageName.parse("openjdk:21-jdk"))
                    .withCopyFileToContainer(
                            MountableFile.forHostPath("../agent/build/libs/agent-all.jar"),
                            "/agent/agent-all.jar")
                    .withEnv("SPRING_APPLICATION_JSON",
                            "{\"server\":{\"address\":\"0.0.0.0\",\"port\":8080}}")
                    .withCopyFileToContainer(
                            MountableFile.forHostPath("../test-app/build/libs/myapp.jar"),
                            "/app/myapp.jar")
                    .withCommand(
                            "java",
                            "-javaagent:/agent/agent-all.jar",
                            "-Dserver.address=0.0.0.0",
                            "-jar", "/app/myapp.jar")
                    .withExposedPorts(8080);

    @Test
    void shouldStartAndInstrumentTestApp() throws Exception {

        String endpoint = "http://" + app.getHost() + ":" + app.getMappedPort(8080) + "/health";
        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        assertEquals(200, conn.getResponseCode());

        String logs = app.getLogs();

        assertTrue(
                logs.contains("loaded: io.jvmsleuths.HealthController")
        );

    }
}
