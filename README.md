Client: ![Client coverage](https://gitlab.ewi.tudelft.nl/cse1105/2020-2021/team-repositories/oopp-group-44/repository-template/badges/master/coverage.svg?job=client-test)
Server: ![Server coverage](https://gitlab.ewi.tudelft.nl/cse1105/2020-2021/team-repositories/oopp-group-44/repository-template/badges/master/coverage.svg?job=server-test)

# AskIT

A live QnA board for direct communication between students and lecturers.

## Description of project
This project was made for the CSE1105 OOP project course. This repository does not capture all branches, pull requests and other version managements that took place in the original repository in GitLab. The backlog of intended features can be found in `docs/Backlog.md` <br/> Most notable functionality:
- Lecturers can create lecture rooms that open on a specific time.
- Users can ask questions, upvote them and give feedback on the lecture's speed. 
- Lecturers can answer questions by moving them to a different tab.
- Moderators can ban users.
- Answered questions can be exported
## Group members

- Dāvis Kažemaks 
- Cherin Kim 
- Emiel Witting 
- ~~Nadja Lazarevic~~ 
- ~~Ebenezer Fosu~~ 

## How to run it
1. Project can be cloned with `git clone git@gitlab.ewi.tudelft.nl:cse1105/2020-2021/team-repositories/oopp-group-44/repository-template.git`
2. Project can be built using the build.gradle file.
### Client
3. The client can be executed with `client/src/main/java/nl/tudelft/oopp/askit/ClientApp.java`
### Server
3. If the service doesn't need to be persistent, then the server can be immediately executed with `server/src/main/java/nl/tudelft/oopp/askit/ServerApplication.java`
4. If the service needs to be persistent, first change the value `dev` to `prod` in `server/src/main/resources/application.properties`
5. Then install PostgreSQL on the server machine.
6. Log in as the superuser in the console, and then execute:
   - `CREATE DATABASE askit;` makes the database
   - `CREATE USER yourUserName WITH PASSWORD 'yourPassword';` creates the user for the database
   - `GRANT ALL PRIVILEGES ON DATABASE askit TO yourUserName` gives necessary privileges.
7. Go to `server/src/main/resources/application-prod.properties` and change
   - `spring.datasource.url=jdbc:postgresql://localhost:5432/askit` to whatever you have it on (if you have the default port, it should work without change).
   - `spring.datasource.username=postgres` change `postgres` to your used user.
   - `spring.datasource.password=root` change `root` to your user password for the user.
8. The app then must be run with `server/src/main/java/nl/tudelft/oopp/askit/ServerApplication.java` once to initialize the database. Next steps are to increase security.
9. After 8. is done properly, the value `spring.jpa.hibernate.ddl-auto=update` should be changed from `update` to `none`.
10. To limit accessibility, log into as superuser into postgreSQL on the console and execute  `REVOKE ALL PRIVILEGES ON DATABASE askit FROM yourUserName;` and then execute `GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO yourUserName;`
11. The server can be executed with `server/src/main/java/nl/tudelft/oopp/askit/ServerApplication.java`

Reference: https://spring.io/guides/gs/accessing-data-mysql/

## How to contribute to it
1. Set up a local environment for the project
2. After adding significant changes, a merge request can be made that adds these changes to the master branch.
3. If the original team members approve of these changes and the changes pass the pipeline and Code of Conduct guidelines, it can be added to the project's source code.

## Copyright / License 
This project is under the MIT license.
