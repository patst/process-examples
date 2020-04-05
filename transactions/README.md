## Transactions

Its important to understand the transaction behavior in order to model the processes correct.

The process instance is executed with the thread which started the process instance until it reaches a *wait state*.

A wait state interrupts the current process instance flow and the instance is continued at a later point in time. 
(Later might be milliseconds, hours, days, ...)
The continuation will use a thread of the process engine JobExecutor thread pool.

Examples for wait states are:

* Human tasks
* events
* async before/ after flag for tasks

Exceptions will cause the process instance to be rolled back to the last wait state. 
If there was no wait state the process instance will completely vanish from the process engines history.