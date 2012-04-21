AjaxSessionExpiration Demo
================

Presentation
------------------

 This is a demo project for Ajax Session Expiration handling.
 
 This demo is a support for the following article
 
  [Spring Security part VI : Session Timeout handling for Ajax calls] (http://doanduyhai.wordpress.com/2012/04/21/spring-security-part-vi-session-timeout-handling-for-ajax-calls/)
 
 
 Installation
------------

- Install [Maven 3](http://maven.apache.org/)
- Compile the project with mvn clean install
- Run Jetty from Maven : mvn jetty:run

 Usage
------------

- Connect to the application at http://127.0.0.1:8080/AjaxSessionExpiration/

The default users are "ddoan/password" but you can change it in the applicationContext-security.xml file

 Note: there is currently no "Logout" link to log out from the application. To logout properly, you can just type http://127.0.0.1:8080/AjaxSessionExpiration/logout
