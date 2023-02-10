package com.example.integrate.spring.react;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.json.JSONException;

@SpringBootApplication
public class SpringBootReactPostgreSQLApplication {

	public static void main(String[] args) {
        try {
        String token1="";
        File fileOne = new File("eur_conversion");
	  File fileOne1 = new File("target/classes/static/eur_conversion");
        token1 = "7,53450";
        token1 = token1.replace("\r\n","");
	  token1 = token1.replace(",",".");
        PrintWriter writer = new PrintWriter(fileOne);
        writer.println(token1);
        writer.close();
        PrintWriter writer1 = new PrintWriter(fileOne1);
        writer1.println(token1);
        writer1.close();
        } catch (IOException e) {
     	  e.printStackTrace();
    	  }
		SpringApplication.run(SpringBootReactPostgreSQLApplication.class, args);
	}

}
