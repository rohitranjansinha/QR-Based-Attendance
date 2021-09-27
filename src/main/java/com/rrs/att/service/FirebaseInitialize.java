package com.rrs.att.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FirebaseInitialize {

    @PostConstruct
    public void Initialize(){

        FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream("./serviceAccountKey.json");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://qr-based-attendance-ac47d-default-rtdb.firebaseio.com")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        FirebaseApp.initializeApp(options);

    }
}
