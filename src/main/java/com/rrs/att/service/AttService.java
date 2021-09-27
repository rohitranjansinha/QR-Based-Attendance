package com.rrs.att.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.rrs.att.model.Attendee;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class AttService {
    private String currentTimestamp(String format){
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        Date date = new Date();
        return dateFormat.format(date);
    }
    private String getGender(String name){
        String[] parts = name.split("-");
        if(parts[0].equals("PB")){
            return "M";
        }else{
            return "F";
        }
    }
    public String persistAttendance(String uid, String name) throws ExecutionException, InterruptedException {
        String timestamp = currentTimestamp("dd/MM/yyyy HH:mm:ss");
        Firestore firestore = FirestoreClient.getFirestore();
        Attendee attendee = new Attendee(uid,timestamp,name,getGender(name));
        ApiFuture<WriteResult> collectionsApuFuture = firestore.collection("attendance").document(attendee.getUID())
                .set(attendee);
        return collectionsApuFuture.get().getUpdateTime().toString();
    }
    public boolean createCSV() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        //asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future = firestore.collection("attendance").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Map<String,Object>> data = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            //System.out.println("doc = "+document.getData());
            data.add(document.getData());
        }
        System.out.println("data = "+data);
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(new File("zonal_attendance.csv"));
            StringBuilder col = new StringBuilder();
            col.append("Date & Time").append(",").append("UID").append(",").append("Name").append(",").append("Gender").append("\n");
            pw.write(col.toString());
            for(int i=0;i<data.size();i++){
                Attendee attendee = new Attendee();
                for(Map.Entry<String,Object> entry : data.get(i).entrySet()){
                    String key = entry.getKey().toString();
                    String val = entry.getValue().toString();
                    switch (key){
                        case "uid":
                            attendee.setUID(val);
                            break;
                        case "gender":
                            attendee.setGender(val);
                            break;
                        case "name":
                            attendee.setName(val);
                            break;
                        case "timestamp":
                            attendee.setTimestamp(val);
                            break;
                    }
                }
                //System.out.println("attendee = "+attendee.toString());
                if(!attendee.getName().equals("HelloWorld")){
                    StringBuilder sb = new StringBuilder();
                    sb.append(attendee.getTimestamp()).append(",")
                            .append(attendee.getUID()).append(",")
                            .append(attendee.getName()).append(",")
                            .append(attendee.getGender()).append("\n");
                    pw.write(sb.toString());
                }
            }
            pw.close();
            return true;
        }catch (Exception e){
            System.out.println("file error  :"+e.getMessage());
            return false;
        }
    }
}
