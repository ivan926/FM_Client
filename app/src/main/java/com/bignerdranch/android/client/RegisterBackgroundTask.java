package com.bignerdranch.android.client;

import android.app.Person;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import Request_Response.EventResponse;
import Request_Response.LoginRequest;
import Request_Response.LoginResponse;
import Request_Response.PersonResponse;
import Request_Response.RegisterRequest;
import Request_Response.RegisterResponse;

public class RegisterBackgroundTask implements Runnable{


    Handler handler = null;
    String hostName = null;
    String portNumber = null;
    String userName = null;
    String password = null;

    String email = null;

    String firstName = null;

    String lastName = null;

    String gender = null;


    RegisterRequest registerRequest = null;
    RegisterResponse registerResponse = null;


    EventResponse eventResponse = null;

    public RegisterBackgroundTask(Handler handler, String hostName, String portNum, String username, String password
                                  ,String email ,String firstName, String lastName, String gender)
    {
        this.handler = handler;
        this.hostName = hostName;
        this.portNumber = portNum;
        this.userName = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    @Override
    public void run() {

        ServerProxy register = new ServerProxy();

        registerRequest = new RegisterRequest(userName,password, email,firstName,lastName,gender );
        try{
            registerResponse = register.register(registerRequest,hostName,Integer.parseInt(portNumber));
            if(registerResponse.isSuccess() == false)
            {
                throw new Exception();
            }
            PersonResponse personResponse = register.getPersonFamily(registerResponse,hostName,Integer.parseInt(portNumber));

            EventResponse eventResponse = register.getAllPersonEvents(registerResponse,hostName,Integer.parseInt(portNumber));

            model.Event[] familyEvents;
            familyEvents = eventResponse.getEventData();

            model.Person[] family;
            family = personResponse.getData();

            dataCache instanceOfDataCache = dataCache.getInstance();

            instanceOfDataCache.setListOfPeople(family,registerResponse.getUsername(),familyEvents,registerResponse.getPersonID());
            sendMessage(family[family.length-1].getFirstName(), family[family.length-1].getLastName());
        }
        catch(Exception E)
        {
            sendMessage(registerResponse.getMessage());
            E.printStackTrace();
        }



        //send message to the handler with information to be updated to UI


    }

    private void sendMessage( String firstname,String lastName) {

        String FIRST_NAME = "firstname";
        String LAST_NAME = "lastname";

        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();

        messageBundle.putString(FIRST_NAME, firstname);

        messageBundle.putString(LAST_NAME, lastName);


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
