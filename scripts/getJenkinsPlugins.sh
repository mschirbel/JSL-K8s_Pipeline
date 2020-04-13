#!/bin/bash
USER="admin"
PASS="11206520c8edb19da64e0f59b1d82d568b"
ADDR="localhost"
PORT="8080"

curl -s http://${USER}:${PASS}@${ADDR}:${PORT}/pluginManager/api/json?depth=1 \
	  | jq -r '.plugins[] | "\(.shortName):\(.version)"' \
	    | sort
