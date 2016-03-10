# Introduction #

The Skype-A-Lizer is a web application to allow you analyze your Skype chats. Basically the app just connects to your "main.db" database in your Skype profile folder and reads out all chat participants plus their messages. To allow this you need to fulfill certain prerequisites explained in the next paragraph.

# Requirements #
  * Java5 compatible Container (Tomcat)
  * SKYPEALIZER\_HOME env. variable set to a path with read/write permissions
  * Skype 4.xx if you want to access logging db while running Skype

# Install #
  1. set SKYPEALIZER\_HOME env. variable
  1. deploy WAR archive to your container