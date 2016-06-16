# Contextproject-BeNine
[![Coverage Status](https://coveralls.io/repos/github/Kingdorian/Contextproject-BeNine/badge.svg?branch=develop)](https://coveralls.io/github/Kingdorian/Contextproject-BeNine?branch=develop)
[![Build Status](https://travis-ci.org/Kingdorian/Contextproject-BeNine.svg?branch=master)](https://travis-ci.org/Kingdorian/Contextproject-BeNine)
## Installation
### Front End
Installing the front-end server:
- Install NodeJS v4.0 from https://nodejs.org 
- Launch terminal, cd to the frontend directory.
- Run `npm install`.

Running the front-end server:
- Run `node app` to start the server.
- You can now visit the server via http://localhost:3000
- Tests can be run by running `npm test`.

### Back end
Build the back-end server using `maven install` with the supplied `pom.xml` file. 

### Deployable zip
#####Unix systems:
Run the `install.sh` script, this generates a zip file, extract this zip file to the desired location on your system. The servers can then be started by executing the `startserver.sh` script. If maven is not installed on your system, run the `other systems` install instructions. 

#####Other systems:
First, fetch the dependencies for the front-end server using `npm install`, then build the back-end server using `maven install` with the supplied `pom.xml` file. 
The zip file containing the backend and frontend server can be found in the `backend/target` folder.
When unzipped the backend and frontend server can both be started using the `startserver.cmd` batch file.

## Tool results
### JavaScript
JavaScript testing on the NodeJS side of the front-end is performed using [MochaJS](http://mochajs.org) and executed using [Istanbul](https://gotwarlost.github.io/istanbul/). Test results are automatically generated on `npm test` and can be found at the `frontend/coverage/lcov-report` folder in the `index.html` file.

### Java
Java testing is performed using JUnit and is executed with Maven. To view a detailed report with PMD, Findbugs and Checkstyle reports, run `mvn site`. Results can be found at the `backend/target/site` folder in the index.html file.

The documentation of the backend HTTP API can be found here: https://github.com/Kingdorian/Contextproject-BeNine/wiki/Backend-HTTP-API-Reference
