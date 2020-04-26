# User Tasks

Examples for different user task variants.

More examples can be found at:
https://github.com/camunda/camunda-bpm-examples/tree/master/usertask 

## Run the example

To run the example start the process application (`mvn spring-boot:run` or launch the main application class).

Then start a new process instance (`Start process`) via task list at http://localhost:8080/app/tasklist/ 

The process instance should then appear in the task list. If not be sure to display all tasks and have no filter, 
which is unfortunately the default settings. 

## Generated task forms

You just have to define attributes with data types and the task form is generated at runtime.
It is possible to define custom datatypes and add validations for the attributes.

The form attributes are defined in the process model. See the `Forms` tab at a user task in the camunda modeller.

## Embedded task forms

Embedded task forms are `.html` files which reside inside the deployment. 
They are located at `src/main/resources/static/forms` and can be reference in the `.bpmn` files with a form key parameter:
`embedded:app:form/FORM_NAME.html`
