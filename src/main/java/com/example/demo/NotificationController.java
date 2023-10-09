package com.example.demo;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;

@RestController
public class NotificationController {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostMapping("sendNotification")
    public String sendNotification(@RequestBody Notification notification) {
        // Delay the execution of the FCM API call by 30 seconds
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            // FCM server endpoint URL
            String url = "https://fcm.googleapis.com/fcm/send";
            String to = "/topics/app";

            // Construct the request body using Notification object
            String requestBody = "{\n" +
                    "    \"to\": \"" + to + "\",\n" +
                    "    \"data\": {\n" +
                    "        \"title\": \"" + notification.getTitle() + "\",\n" +
                    "        \"description\": \"" + notification.getDescription() + "\",\n" +
                    "        \"type\": \"" + notification.getType() + "\",\n" +
                    "     }\n" +
                    "}";

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "key=AAAA7PPMAfM:APA91bETH64DmjvloDsSxsNqFSRFr4f791pUUWBsQFT8E_fhDpQBDld63-lnMDf8fjResKiq0V_VMoOOmCHMemuOTbOvEOCYu1UQiR9ucCpd0h9r_z6xZOqscuOumNm9WJEoRI7g6lQw");

            // Create the request entity
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // Create RestTemplate
            RestTemplate restTemplate = new RestTemplate();
            // Send POST request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            // Print response
            System.out.println("Response: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
        }, 30, TimeUnit.SECONDS);

        // Return a response immediately (before the API call completes)
        return "Notification request will be sent after 30 seconds.";
    }
}
