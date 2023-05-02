package com.bignerdranch.android.client;

import android.app.Person;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import Request_Response.EventRequest;
import Request_Response.EventResponse;
import Request_Response.LoginRequest;
import Request_Response.LoginResponse;
import Request_Response.PersonRequest;
import Request_Response.PersonResponse;

public class loginBackGroundTask implements Runnable {
   Handler handler = null;
   String hostName = null;
   String portNumber = null;
   String userName = null;
   String password = null;
   LoginRequest loginRequest = null;
   LoginResponse loginResponse = null;


   EventResponse eventResponse;

   EventRequest eventRequest;

   PersonResponse personResponse;

   PersonRequest personRequest;


   public loginBackGroundTask(Handler handler, String hostName, String portNum, String username, String password)
   {
       this.handler = handler;
       this.hostName = hostName;
       this.portNumber = portNum;
       this.userName = username;
       this.password = password;
   }

    @Override
    public void run() {

       ServerProxy login = new ServerProxy();

       loginRequest = new LoginRequest(userName,password);
       try{
           loginResponse = login.login(loginRequest,hostName,Integer.parseInt(portNumber));
            if(loginResponse.getAuthtoken() == null)
            {
                throw new Exception();
            }
           personResponse = login.getPersonFamily(loginResponse,hostName, Integer.valueOf(portNumber));

            eventResponse = login.getAllPersonEvents(loginResponse,hostName,Integer.valueOf(portNumber));

            model.Event[] familyEvents;
            familyEvents = eventResponse.getEventData();


           model.Person[] family;
           family = personResponse.getData();

           dataCache instanceOfDataCache = dataCache.getInstance();

           instanceOfDataCache.setListOfPeople(family,loginResponse.getUsername(),familyEvents);
           sendMessage(family[family.length-1].getFirstName(), family[family.length-1].getLastName());
       }
       catch(Exception E)
       {
           sendMessage(loginResponse.getMessage());
           System.out.println("There was an error when trying to log in");

           E.printStackTrace();
       }


       //send message to the handler with information to be updated to UI


    }

    private void sendMessage( String firstname, String lastname) {

       String FIRST_NAME = "firstname";
        String LAST_NAME = "lastname";

       Message message = Message.obtain();

        Bundle messageBundle = new Bundle();

        messageBundle.putString(FIRST_NAME, firstname);

        messageBundle.putString(LAST_NAME, lastname);


        message.setData(messageBundle);

        handler.sendMessage(message);
    }

    private void sendMessage( String error) {

        String ERROR_MESSAGE = "error";


        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();

        messageBundle.putString(ERROR_MESSAGE, error);




        message.setData(messageBundle);

        handler.sendMessage(message);
    }
}
