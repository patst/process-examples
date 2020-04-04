# Delegates

Show how the different delegate styles work.

Delegate styles:

* Java Class (Full qualified classname), e.g `de.fhdw.process.MyDelegate`
* Expression to do something directly, e.g. `${execution.setVariable('test', 'foo')}`
* Delegate Expression to point to a Java Delegate implementation, e.g. `${myDelegate}`
* External task for tasks which will be fulfilled by some external task implementation


## External task delegate

For the external task example a `@EnableScheduling` annotation is added to be able to use scheduled quartz jobs.

