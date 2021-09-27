package com.rrs.att.controllers;

import com.rrs.att.service.AttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class Controller {

    @Autowired
    AttService service;

    @RequestMapping("/")
    public String welcome(){
        return "<h1>Hey</h1>";
    }

    @GetMapping("/markmyattendance")
    public String markAttendance(@RequestParam String uid, @RequestParam String name){
        //System.out.println("uid caught = "+uid);
        try{
            String timestamp = service.persistAttendance(uid,name);
            return "<center><h1>Attendance Marked Successfully</h1></center>";
        }catch (Exception e){
            System.out.println("error: "+e.getMessage());
            return "<center><h1>Try again</h1></center>";
        }
    }

    @RequestMapping("/getcsv")
    public String getCSV(){
        try{
            service.createCSV();
            return "<center><h1>check console</h1></center>";
        }catch (Exception e){
            return "<center><h1>Some error</h1></center>";
        }
    }
}
