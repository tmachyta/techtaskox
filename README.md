# techtaskox

# 💵💵SalesSync💵💵

# Project Description:
- 🫡Welcome to the SalesSync project!🫡
- SalesSync is a web application built with Spring Boot that implements basic CRUD operations for managing data. 
- It also features a WebSocket configuration for real-time updates, -allowing efficient task status synchronization. 
- The frontend is developed using Thymeleaf, providing an intuitive and dynamic user interface. 
- This solution is ideal for tracking and synchronizing information in real time.

## Setup

To set up the project, follow these steps:

### Prerequisites

Make sure you have the following software installed on your system:

- Java Development Kit (JDK) 17 or higher
- Spring Boot 3.4.1 or higher
- Apache Maven
- Apache Tomcat vesion 9 or higher
- DataBase: Postgres

### Installation
- First of all, you should made your fork
- Second, clink on Code<> and clone link, after that open your Intellij Idea, click on Get from VCS
- past link, which you clone later

### Replace Placeholders:
To connect to your DB, you should replace PlaceHolders in application.properties
- Open package resources and open file application.properties in your project.
- Locate the placeholders that need to be replaced.
- These placeholders might include values such as
- jdbc:postgresql://postgresdb:$HOST/$DB -> Change HOST to your host and DB to your DB name
- $POSTGRES_USER -> Change to your username to DB
- $POSTGRES_PASSWORD -> Change to your password to DB
  
# Features 🤌:

## User 🤵‍♂️
- Registration
- Login
- Authentication by JWT

## Role 🙎‍♂️
- Create/update/remove a role
- Get role by roleName

## Client 👨‍⚖️
- Create client
- Soft-Delete client
- Update client information
- Get list of clients
- Get client by ID
- Get clients by params

## Contact 👨‍🚀
- Create contact
- Soft-Delete contact
- Update contact information
- Get list of clients
- Get contact by ID
- Get contacts by params
- Update contact client by ID

## Task 🗒
- Create task
- Soft-Delete task
- Update task by ID
- Get list of tasks
- Get task by ID
- Update task status by ID
- Update task contact by ID

## Notification 📫
- Send notification about changing task status
- Send notification about changin task contact

## Controllers 🕹

## AuthController 🤵‍♂️
- Sign in -> /auth/login - Post
- Sign up -> /auth/register - Post

## Client 👨‍⚖️
- Save client -> /clients - Post
- Get list of clients -> /clients - Get
- Get client by ID -> /clients/{id} - Get
- Soft-delete client by ID -> /clients/{id} - Delete
- Update client information by ID -> /clients/{id} - Put
- Search clients by params -> /clients/search? names/fields - Get

## Contact 👨‍🚀
- Save contact -> /contacts - Post
- Get list of contacts -> /contacts - Get
- Get contact by ID -> /contacts/{id} - Get
- Soft-delete contact by ID -> /contacts/{id} - Delete
- Update contact information by ID -> /contacts/{id} - Put
- Search contacts by params -> /contacts/search? names/lastNames - Get
- Update contact client by ID -> /contacts/client/{id} - Put

## Task 🗒
- Save task -> /tasks - Post
- Get list of tasks -> /tasks - Get
- Get task by ID -> /tasks/{id} - Get
- Soft-delete task by ID -> /tasks/{id} - Delete
- Update task information by ID -> /tasks/{id} - Put
- Update task contact by ID (Using WebSocket to get Notification about changing contact) -> /tasks/contact/{id} - Put
- Update task status by ID (Using WebSocket to get Notification about changing status) -> /tasks/status/{id} - Put

## LoginRegController 🕹
- Login/Registration page - /login-registration

## AuthFunctionalController 🕹
- Main page - /main-page
- Add client - /add-client
- Add contact - /add-contact
- Add task - /add-task

## Another Pages 📄
- clientslist.html
- contactlist.html
- taskslist.html
- contacttask.html
- status.html
- taskcontact.html
- updateclient.html
- updatecontact.html
- updatecontactclient.html
- updatetask.html

