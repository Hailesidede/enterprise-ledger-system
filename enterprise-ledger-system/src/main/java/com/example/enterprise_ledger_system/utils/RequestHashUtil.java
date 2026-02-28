package com.example.enterprise_ledger_system.utils;

import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class RequestHashUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String sha256(Object request) {
        try{
            String json = mapper.writeValueAsString(request);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(json.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();

            for(byte b : hash){
                hex.append(String.format("%02x", b));
            }

            return hex.toString();
        }catch(Exception e){
            throw new RuntimeException("Failed to hash request", e);
        }
    }
}
