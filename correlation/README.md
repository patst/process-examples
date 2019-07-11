# Correlation

A send task and a event represent an asynchronous service call.

The send task calls a service which does some processing and calls a REST endpoint with the result.

The REST endpoint must then find the process instance which sent the request and set the result as variable.
Then the event must be fired.