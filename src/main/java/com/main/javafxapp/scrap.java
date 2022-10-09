package com.main.javafxapp;

import com.main.javafxapp.Controllers.LoginController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class scrap {

    public static ZoneId scrapZoneID;

    public static void main(String[] args) {
        scrapZoneID = ZoneId.systemDefault();


        Instant instant = Instant.now();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));
        String formattedDate = dateTimeFormatter.format(instant);

        System.out.println(formattedDate);
    }
}
