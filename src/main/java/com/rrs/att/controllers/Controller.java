package com.rrs.att.controllers;

import com.rrs.att.service.AttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

@RestController
public class Controller {

    private final String credits = "<p>\n\n</p><center><h4>This attendance system was developed by Rohit Ranjan Sinha<p>\n</p>-Varanasi Branch Satsang</h4></center>";

    @Autowired
    AttService service;

    @RequestMapping("/")
    public String welcome(){
        return "<center><h1>Welcome to QR Based Attendance System</h1></center>"+credits;
    }

    @RequestMapping("/markmyattendance/{uid}/{name}")
    public String markAttendance(@PathVariable String uid, @PathVariable String name){
        System.out.println("uid caught: "+uid+" name caught: "+name);
        try{
            String timestamp = service.persistAttendance(uid,name);
            if(timestamp==null){
                return "<center><h1>Your attendance has already been marked successfully !</h1></center>"+credits;
            }if(timestamp.equals("")){
                return "<center><h1>Some error has occurred, please try again later</h1></center>"+credits;
            }
            return "<center><h1>Attendance Marked Successfully !!</h1></center>"+credits;
        }catch (Exception e){
            System.out.println("error: "+e.getMessage());
            return "<center><h1>Try again</h1></center>";
        }
    }

    @RequestMapping("/download")
    public String downloadCSVFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            service.createCSV();

            File file = new File("./zonal_attendance.csv");
            if (file.exists()) {
                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                if (mimeType == null) {
                    //unknown mimetype so set the mimetype to application/octet-stream
                    mimeType = "application/octet-stream";
                }
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

                FileCopyUtils.copy(inputStream, response.getOutputStream());
                response.setContentLength((int) file.length());
                return "<center><h1>File downloaded to your system</h1></center>"+credits;
            }else{
                return "<center><h1>Some error has occured, please try again</h1></center>"+credits;
            }
        }catch (Exception e){
            return "<center><h1>Some error has occured, please try again</h1></center>"+credits;
        }
    }
}
