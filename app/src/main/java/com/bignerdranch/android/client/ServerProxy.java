package com.bignerdranch.android.client;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Request_Response.EventRequest;
import Request_Response.EventResponse;
import Request_Response.LoginRequest;
import Request_Response.LoginResponse;
import Request_Response.PersonRequest;
import Request_Response.PersonResponse;
import Request_Response.RegisterRequest;
import Request_Response.RegisterResponse;
import model.authtoken;

public class ServerProxy {

    public static void main(String[] args)
    {
        String hostName = args[1];
        Integer portNumber = Integer.parseInt(args[2]);

       // login(null,hostName,portNumber);
    }

    public LoginResponse login(LoginRequest loginRequest,String hostname,Integer portNumber)
    {   Gson gson;
        gson = new Gson();

        LoginResponse loginResponse = new LoginResponse();

        String json = gson.toJson(loginRequest,LoginRequest.class);

        try{
            URL url = new URL( "http://" + hostname + ":" + portNumber + "/user/login");
            HttpURLConnection server = (HttpURLConnection) url.openConnection();

            server.setRequestMethod("POST");
            server.setDoOutput(true);

            server.addRequestProperty("Accept","application/json");
            //before we send our Json data we must first establish a connection
            server.connect();
            OutputStream outputStream = server.getOutputStream();
            //we are converting our json to the bytes sent to the server
            writeString(json,outputStream);
            //by closing we are sending the request body to the server
            outputStream.close();

            if(server.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream responseBody = server.getInputStream();
                String response = readString(responseBody);

                loginResponse = gson.fromJson(response,loginResponse.getClass());
                //System.out.println("Logged in successfully");
            }
            else
            {


                System.out.println("ERROR: " + server.getResponseCode());

                InputStream responseBody = server.getErrorStream();


                String responseData = readString(responseBody);

                loginResponse = gson.fromJson(responseData,loginResponse.getClass());

               // System.out.println(responseData);

            }

        }
         catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return loginResponse;

    }

    public RegisterResponse register(RegisterRequest registerRequest,String hostname,Integer portNumber)
    {
        Gson gson;
        gson = new Gson();

        RegisterResponse registerResponse = new RegisterResponse();

        String json = gson.toJson(registerRequest,RegisterRequest.class);

        try{
            URL url = new URL( "http://" + hostname + ":" + portNumber + "/user/register");
            HttpURLConnection server = (HttpURLConnection) url.openConnection();

            server.setRequestMethod("POST");
            server.setDoOutput(true);

            server.addRequestProperty("Accept","application/json");
            //before we send our Json data we must first establish a connection
            server.connect();
            OutputStream outputStream = server.getOutputStream();
            //we are converting our json to the bytes sent to the server
            writeString(json,outputStream);
            //by closing we are sending the request body to the server
            outputStream.close();

            if(server.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream responseBody = server.getInputStream();
                String response = readString(responseBody);

                registerResponse = gson.fromJson(response,registerResponse.getClass());
                //System.out.println("Logged in successfully");
            }
            else
            {


                System.out.println("ERROR: " + server.getResponseCode());

                InputStream responseBody = server.getErrorStream();


                String responseData = readString(responseBody);

                registerResponse = gson.fromJson(responseData,registerResponse.getClass());

                // System.out.println(responseData);

            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return registerResponse;
    }


    public PersonResponse personID(RegisterResponse registerResponse, String hostname, Integer portNumber)
    {   Gson gson;
        gson = new Gson();
        authtoken AuthToken = new authtoken(registerResponse.getAuthtoken(), registerResponse.getUsername());
        PersonRequest personRequest = new PersonRequest(AuthToken);

        PersonResponse personResponse = new PersonResponse();

        String personID = registerResponse.getPersonID();
        String json = gson.toJson(personRequest,PersonRequest.class);

        try{
            URL url = new URL( "http://" + hostname + ":" + portNumber + "/person/"+ personID);
            HttpURLConnection server = (HttpURLConnection) url.openConnection();
            server.setDoOutput(false);
            server.setRequestMethod("GET");
            server.setRequestProperty("Authorization",registerResponse.getAuthtoken());




            //before we send our Json data we must first establish a connection
            server.connect();
          //  OutputStream outputStream = server.getOutputStream();
            //we are converting our json to the bytes sent to the server
           // writeString(json,outputStream);
            //by closing we are sending the request body to the server
           // outputStream.close();

            if(server.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream responseBody = server.getInputStream();
                String response = readString(responseBody);

                personResponse = gson.fromJson(response,PersonResponse.class);
                //System.out.println("Logged in successfully");
            }
            else
            {


                System.out.println("ERROR: " + server.getResponseCode());

                InputStream responseBody = server.getErrorStream();


                String responseData = readString(responseBody);

                personResponse = gson.fromJson(responseData,PersonResponse.class);

                // System.out.println(responseData);

            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return personResponse;

    }


    public PersonResponse personID(LoginResponse loginResponse, String hostname, Integer portNumber)
    {   Gson gson;
        gson = new Gson();
        authtoken AuthToken = new authtoken(loginResponse.getAuthtoken(), loginResponse.getUsername());
        PersonRequest personRequest = new PersonRequest(AuthToken);

        PersonResponse personResponse = new PersonResponse();

        String personID = loginResponse.getPersonID();
        String json = gson.toJson(personRequest,PersonRequest.class);

        try{
            URL url = new URL( "http://" + hostname + ":" + portNumber + "/person/"+ personID);
            HttpURLConnection server = (HttpURLConnection) url.openConnection();
            server.setDoOutput(false);
            server.setRequestMethod("GET");
            server.setRequestProperty("Authorization",loginResponse.getAuthtoken());




            //before we send our Json data we must first establish a connection
            server.connect();
            //  OutputStream outputStream = server.getOutputStream();
            //we are converting our json to the bytes sent to the server
            // writeString(json,outputStream);
            //by closing we are sending the request body to the server
            // outputStream.close();

            if(server.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream responseBody = server.getInputStream();
                String response = readString(responseBody);

                personResponse = gson.fromJson(response,PersonResponse.class);
                //System.out.println("Logged in successfully");
            }
            else
            {


                System.out.println("ERROR: " + server.getResponseCode());

                InputStream responseBody = server.getErrorStream();


                String responseData = readString(responseBody);

                personResponse = gson.fromJson(responseData,PersonResponse.class);

                // System.out.println(responseData);

            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return personResponse;

    }


    public PersonResponse getPersonFamily(LoginResponse loginResponse, String hostname, Integer portNumber)
    {   Gson gson;
        gson = new Gson();
        authtoken AuthToken = new authtoken(loginResponse.getAuthtoken(), loginResponse.getUsername());
        PersonRequest personRequest = new PersonRequest(AuthToken);

        PersonResponse personResponse = new PersonResponse();

        String personID = loginResponse.getPersonID();
        String json = gson.toJson(personRequest,PersonRequest.class);

        try{
            URL url = new URL( "http://" + hostname + ":" + portNumber + "/person");
            HttpURLConnection server = (HttpURLConnection) url.openConnection();
            server.setDoOutput(false);
            server.setRequestMethod("GET");
            server.setRequestProperty("Authorization",loginResponse.getAuthtoken());




            //before we send our Json data we must first establish a connection
            server.connect();
            //  OutputStream outputStream = server.getOutputStream();
            //we are converting our json to the bytes sent to the server
            // writeString(json,outputStream);
            //by closing we are sending the request body to the server
            // outputStream.close();

            if(server.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream responseBody = server.getInputStream();
                String response = readString(responseBody);

                personResponse = gson.fromJson(response,PersonResponse.class);
                //System.out.println("Logged in successfully");
            }
            else
            {


                System.out.println("ERROR: " + server.getResponseCode());

                InputStream responseBody = server.getErrorStream();


                String responseData = readString(responseBody);

                personResponse = gson.fromJson(responseData,PersonResponse.class);

                // System.out.println(responseData);

            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return personResponse;

    }


    public PersonResponse getPersonFamily(RegisterResponse registerResponse, String hostname, Integer portNumber)
    {   Gson gson;
        gson = new Gson();
        authtoken AuthToken = new authtoken(registerResponse.getAuthtoken(), registerResponse.getUsername());
        PersonRequest personRequest = new PersonRequest(AuthToken);

        PersonResponse personResponse = new PersonResponse();

        String personID = registerResponse.getPersonID();
        String json = gson.toJson(personRequest,PersonRequest.class);

        try{
            URL url = new URL( "http://" + hostname + ":" + portNumber + "/person");
            HttpURLConnection server = (HttpURLConnection) url.openConnection();
            server.setDoOutput(false);
            server.setRequestMethod("GET");
            server.setRequestProperty("Authorization",registerResponse.getAuthtoken());




            //before we send our Json data we must first establish a connection
            server.connect();
            //  OutputStream outputStream = server.getOutputStream();
            //we are converting our json to the bytes sent to the server
            // writeString(json,outputStream);
            //by closing we are sending the request body to the server
            // outputStream.close();

            if(server.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream responseBody = server.getInputStream();
                String response = readString(responseBody);

                personResponse = gson.fromJson(response,PersonResponse.class);
                //System.out.println("Logged in successfully");
            }
            else
            {


                System.out.println("ERROR: " + server.getResponseCode());

                InputStream responseBody = server.getErrorStream();


                String responseData = readString(responseBody);

                personResponse = gson.fromJson(responseData,PersonResponse.class);

                // System.out.println(responseData);

            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return personResponse;

    }




    public EventResponse getAllPersonEvents(LoginResponse loginResponse, String hostname, Integer portNumber)
    {   Gson gson;
        gson = new Gson();
        authtoken AuthToken = new authtoken(loginResponse.getAuthtoken(), loginResponse.getUsername());
        EventRequest eventRequest = new EventRequest(AuthToken);

        EventResponse eventResponse = new EventResponse();


        String json = gson.toJson(eventRequest,EventRequest.class);

        try{
            URL url = new URL( "http://" + hostname + ":" + portNumber + "/event");
            HttpURLConnection server = (HttpURLConnection) url.openConnection();
            server.setDoOutput(false);
            server.setRequestMethod("GET");
            server.setRequestProperty("Authorization",loginResponse.getAuthtoken());




            //before we send our Json data we must first establish a connection
            server.connect();
            //  OutputStream outputStream = server.getOutputStream();
            //we are converting our json to the bytes sent to the server
            // writeString(json,outputStream);
            //by closing we are sending the request body to the server
            // outputStream.close();

            if(server.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream responseBody = server.getInputStream();
                String response = readString(responseBody);

                eventResponse = gson.fromJson(response,EventResponse.class);
                //System.out.println("Logged in successfully");
            }
            else
            {


                System.out.println("ERROR: " + server.getResponseCode());

                InputStream responseBody = server.getErrorStream();


                String responseData = readString(responseBody);

                eventResponse = gson.fromJson(responseData, EventResponse.class);

                // System.out.println(responseData);

            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return eventResponse;

    }


    public EventResponse getAllPersonEvents(RegisterResponse registerResponse, String hostname, Integer portNumber)
    {   Gson gson;
        gson = new Gson();
        authtoken AuthToken = new authtoken(registerResponse.getAuthtoken(), registerResponse.getUsername());
        EventRequest eventRequest = new EventRequest(AuthToken);

        EventResponse eventResponse = new EventResponse();


        String json = gson.toJson(eventRequest,EventRequest.class);

        try{
            URL url = new URL( "http://" + hostname + ":" + portNumber + "/event");
            HttpURLConnection server = (HttpURLConnection) url.openConnection();
            server.setDoOutput(false);
            server.setRequestMethod("GET");
            server.setRequestProperty("Authorization",registerResponse.getAuthtoken());




            //before we send our Json data we must first establish a connection
            server.connect();
            //  OutputStream outputStream = server.getOutputStream();
            //we are converting our json to the bytes sent to the server
            // writeString(json,outputStream);
            //by closing we are sending the request body to the server
            // outputStream.close();

            if(server.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream responseBody = server.getInputStream();
                String response = readString(responseBody);

                eventResponse = gson.fromJson(response,EventResponse.class);
                //System.out.println("Logged in successfully");
            }
            else
            {


                System.out.println("ERROR: " + server.getResponseCode());

                InputStream responseBody = server.getErrorStream();


                String responseData = readString(responseBody);

                eventResponse = gson.fromJson(responseData, EventResponse.class);

                // System.out.println(responseData);

            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return eventResponse;

    }








    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {

        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }


}
