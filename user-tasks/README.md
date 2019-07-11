# User Tasks

Examples for different user task variants.

More examples can be found at:
https://github.com/camunda/camunda-bpm-examples/tree/master/usertask 

## Generated task forms

You just have to define attributes with data types and the task form is generated at runtime.
It is possible to define custom datatypes and add validations for the attributes.

## Embedded task forms

Embedded task forms are `.html` files which reside inside the deployment. 
They are located at `src/main/resources/static/forms` and can be reference in the `.bpmn` files with a form key parameter:
`embedded:app:form/FORM_NAME.html`