package com.rrs.att.controllers;

import com.rrs.att.service.AttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

@RestController
public class Controller {

    @Autowired
    AttService service;

    @RequestMapping("/")
    public String welcome(){
        return "<h1>Hey</h1>";
    }

//    @RequestMapping("/test")
//    public String hey(){ service.tempFunc(); return "<h1>done</h1>";}

    @GetMapping("/markmyattendance")
    public String markAttendance(@RequestParam String uid, @RequestParam String name){
        //System.out.println("uid caught = "+uid);
        try{
            String timestamp = service.persistAttendance(uid,name);
            if(timestamp==null){
                return "<center><h1>Your attendance has already been marked successfully</h1></center>";
            }if(timestamp.equals("")){
                return "<center><h1>Some error has occurred, please try again later</h1></center>";
            }
            return "<center><h1>Attendance Marked Successfully</h1></center>";
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
                return "<center><h1>File downloaded to your system</h1></center>";
            }else{
                return "<center><h1>Some error has occured, please try again</h1></center>";
            }
        }catch (Exception e){
            return "<center><h1>Some error has occured, please try again</h1></center>";
        }
    }
}
