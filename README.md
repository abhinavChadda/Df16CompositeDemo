# DreamforceDemo

A barebones Java app which contains a couple of examples of How to use Salesforce's Composite API and demonstrates the benefits of using said API.

## Running Locally
Import the project into Eclipse and you should be able to run it from there.

Make sure you have Java and Maven installed. 
Set the following env variables
 1. "USERNAME" to your salesforce's dev org
 2. "PASSWORD" to your Salesforce's dev org's user. Make sure you append the API token to you password.
 3. "PORT" to the port you want the app to run on.
 4. "ISDEBUGGING" to true if you want to see debugging statements in your console
 5. "ISDEBUGGINGSERIALIZATION" to true if you want to see the serialized Request and Response in your console. (ISDEBUGGING is required for this.)


WHEN YOU RUN THE PROJECT YOU CAN ACCESS IT FROM "localhost:$PORT/home"