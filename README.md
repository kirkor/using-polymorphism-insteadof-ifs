# Using Polymorphism Instead Of ifs
Simple example how to create runtime injection in Spring Framework

## What is this
We need to use different services implementation depends on user context. I've create simple mechanism based on spring ProxyFactoryBean. During the runtime when method of the service is invoked we are making decision witch implementation I want to use. 

Concept is more or less described by this image: 

<img src="https://raw.githubusercontent.com/kirkor/using-polymorphism-insteadof-ifs/master/src/main/java/com/mt/hybris/services/Interfaces.jpg" />

## Main condition
### Use polymorphism instead of IFS my friend
### Make it as simple to use as it's possible
### Choose implementation in runtime – runtime injection
