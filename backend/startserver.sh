#!/bin/bash
(
	(java -jar backend-0.1.0.jar | sed -e 's/^/[Backend]/') &
	(cd frontend &&  node app.js | sed -e 's/^/[Frontend]/')
)
