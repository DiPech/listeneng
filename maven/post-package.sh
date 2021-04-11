#!/usr/bin/env bash

cp target/ROOT.war "docker/app/dist/app.war"
cp conf/app.prod.env "docker/app/dist/app.env"
