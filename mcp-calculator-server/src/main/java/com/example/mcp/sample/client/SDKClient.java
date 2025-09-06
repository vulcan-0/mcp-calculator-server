package com.example.mcp.sample.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.WebFluxSseClientTransport;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public class SDKClient {

    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(WebClient.builder().baseUrl("http://localhost:8080"));
        new SDKClient(transport).run();
    }

    private final McpClientTransport transport;

    public SDKClient(McpClientTransport transport) {
        this.transport = transport;
    }

    public void run() {
        try (var client = McpClient.sync(this.transport).build()) {
            client.initialize();

            // List and demonstrate tools
            ListToolsResult toolsList = client.listTools();
            System.out.println("Available Tools = " + toolsList);

            // You can also ping the server to verify connection
            client.ping();

            // Call various calculator tools
            CallToolResult resultAdd = client.callTool(new CallToolRequest("add", Map.of("a", 5.0, "b", 3.0)));
            System.out.println("Add Result = " + resultAdd);

            CallToolResult resultSubtract = client.callTool(new CallToolRequest("subtract", Map.of("a", 10.0, "b", 4.0)));
            System.out.println("Subtract Result = " + resultSubtract);

            CallToolResult resultMultiply = client.callTool(new CallToolRequest("multiply", Map.of("a", 6.0, "b", 7.0)));
            System.out.println("Multiply Result = " + resultMultiply);

            CallToolResult resultDivide = client.callTool(new CallToolRequest("divide", Map.of("a", 20.0, "b", 4.0)));
            System.out.println("Divide Result = " + resultDivide);

            CallToolResult resultHelp = client.callTool(new CallToolRequest("help", Map.of()));
            System.out.println("Help = " + resultHelp);
        }
    }

}
