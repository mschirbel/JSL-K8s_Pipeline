#!/bin/bash
USER="admin"
PASS="11206520c8edb19da64e0f59b1d82d568b"
ADDR="localhost"
PORT="8080"
JOBN="tste"

curl -s http://${USER}:${PASS}@${ADDR}:${PORT}/job/${JOBN}/config.xml
