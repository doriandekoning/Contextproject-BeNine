#!/bin/bash
echo 'Installing server'
cd frontend
npm install
cd ../backend
mvn install
cd ..
cp backend/target/backend-server-webinterface.zip server.zip
