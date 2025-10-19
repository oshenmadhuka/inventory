package com.ga.binpacking.agent;

import com.ga.binpacking.model.PackingSolution;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * OpenAI-powered agent for advanced reasoning about packing solutions
 * This agent can use GPT models to provide insights and recommendations
 * 
 * Note: Requires OPENAI_API_KEY environment variable to be set
 */
public class OpenAIAgent {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-4"; // or "gpt-3.5-turbo"

    private final String apiKey;
    private final OkHttpClient client;

    public OpenAIAgent() {
        this.apiKey = System.getenv("OPENAI_API_KEY");
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Check if OpenAI API is configured
     */
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty();
    }

    /**
     * Get AI-powered insights about the packing solution
     */
    public String getInsights(PackingSolution solution, String binDescription) {
        if (!isConfigured()) {
            return "OpenAI API key not configured. Set OPENAI_API_KEY environment variable.";
        }

        try {
            String prompt = buildPrompt(solution, binDescription);
            return callOpenAI(prompt);
        } catch (IOException e) {
            return "Error communicating with OpenAI: " + e.getMessage();
        }
    }

    /**
     * Build a prompt for the OpenAI API
     */
    private String buildPrompt(PackingSolution solution, String binDescription) {
        return String.format(
                "You are an expert in logistics and bin packing optimization. " +
                        "Analyze the following 3D bin packing solution and provide insights:\n\n" +
                        "Bin Configuration: %s\n" +
                        "Items Packed: %d\n" +
                        "Total Wastage: %d cubic units\n" +
                        "Total Value: $%.2f\n" +
                        "Fitness Score: %.4f\n\n" +
                        "Please provide:\n" +
                        "1. An assessment of the solution quality\n" +
                        "2. Potential improvements\n" +
                        "3. Trade-offs between space and value\n" +
                        "4. Practical considerations for implementation",
                binDescription,
                solution.getPlacements().size(),
                solution.getTotalWastage(),
                solution.getTotalCost(),
                solution.getFitness());
    }

    /**
     * Call OpenAI API
     */
    private String callOpenAI(String prompt) throws IOException {
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", MODEL);
        requestBody.add("messages", new com.google.gson.JsonArray());
        requestBody.getAsJsonArray("messages").add(message);
        requestBody.addProperty("max_tokens", 500);
        requestBody.addProperty("temperature", 0.7);

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("OpenAI API error: " + response.code());
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            return jsonResponse
                    .getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        }
    }

    /**
     * Get recommendations using AI
     */
    public String getRecommendations(String context) {
        if (!isConfigured()) {
            return "OpenAI integration not available. Using rule-based recommendations.";
        }

        try {
            String prompt = "Based on this bin packing context: " + context +
                    "\nProvide 3 specific, actionable recommendations for improvement.";
            return callOpenAI(prompt);
        } catch (IOException e) {
            return "Error getting AI recommendations: " + e.getMessage();
        }
    }
}
