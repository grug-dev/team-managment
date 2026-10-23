# Team Managment

[![CI](https://github.com/grug-dev/team-managment/actions/workflows/ci.yml/badge.svg)](https://github.com/grug-dev/team-managment/actions/workflows/ci.yml)

This project solves the following problem:

```
In a team of n people, a celebrity is known by everyone but he/she doesn't know anybody.
```

## Prerequisites

The solution uses Java JDK 8 and Spring Boot 2.0.4.RELEASE. Check your system:

* `JAVA_HOME` points to a Java 8 installation. Check it with `java -version`.
* Maven is installed. Check it with `mvn -version`.

## Application Layers

![Application layers](doc/application_layer.png)

## Responsibilities

#### TeamResource

REST controller. It receives the uploaded file with the test cases and returns a JSON object
with the result of each test case. It only handles HTTP.

#### TestCasesFileParser

Reads the uploaded file and builds the test cases. It returns a `400 Bad Request` with a clear
message when the file has a bad format.

#### CelebrityFinderServiceImpl

Implements `TeamMemberFinderService`. It finds the celebrity of a team: the member who knows
nobody and is known by every other member.

###### **Request Info**

+ **HTTP Method:** POST
+ **URI:** `/v1/teams/upload/filterByCelebrity`
+ **Content-Type:** `multipart/form-data`
+ **Form field:** `file` (a plain text file)

## How Can I Test It?

Upload a file with the test cases. The file has the members of each team. The input format
explains it.

### Input Format

The first line contains an integer T, the number of test cases. T test cases follow.

Each test case starts with a line with two integers N and M, separated by a space. N is the
team ID and M is the number of members of the team.

```
1 5  [ [N,M] = Team Id, members of the Team ]
```

The next M lines contain, separated by a space:

```
[J, K...] = Member Id, Ids of the members that J knows
```

### Output Format

The service returns a JSON document with the result of each test case.

### Sample Input

```
2
1 5
1
2 1 3
3 1 4
4 1 2
5 1 3
2 3
1 1
2 1
3 1
```

### Sample Output

```json
{
  "testCase": [
    {
      "idTeam": 1,
      "celebrity": 1,
      "message": "Celebrity Found with ID: 1"
    },
    {
      "idTeam": 2,
      "celebrity": -1,
      "message": "Celebrity not found"
    }
  ]
}
```

### Explanation

There are two test cases. The first team (ID 1) has five members. The second team (ID 2) has
three members.

In the first team, member 1 knows nobody, and all the other members know member 1. Member 1 is
the celebrity.

In the second team, all members know member 1, but member 1 also knows himself. There is no
celebrity.

## Running the Solution

Go to the root folder of the project and run:

```
mvn spring-boot:run
```

Run the tests with:

```
mvn test
```

## Uploading a Test File

The server port is 8585. The REST documentation uses Swagger UI. Open this URL in your browser:

```
http://localhost:8585/swagger-ui.html
```

Find the resource `POST /v1/teams/upload/filterByCelebrity` and upload the file with the test
cases.

![Upload file in Swagger](doc/uploadfile_swagger.png)

[Data Test Cases Example](doc/data.txt)
