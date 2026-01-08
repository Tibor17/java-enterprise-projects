#!/bin/sh

# Performance test:
# h2load --h1 -c 100 -n 100000 http://localhost:8080/actuator/healthcheck

# Healthcheck response
curl -X GET http://localhost:8080/windfinder/api/v0.1/forecast/dates/2025-11-03


