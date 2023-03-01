#!/usr/bin/env bash

mvn -f ./.. clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    ./../target/demo-0.0.1-SNAPSHOT.jar \
    Shulpov@185.249.227.104:/home/Shulpov/

echo 'Restart server...'

echo '\t authentication...'
ssh -i ~/.ssh/id_rsa Shulpov@185.249.227.104 << EOF

echo '\t terminate the process if it is running...'
pgrep java | xargs kill -9

echo '\t executing .jar and writing logs...'
nohup java -jar ./demo-0.0.1-SNAPSHOT.jar > log.txt &

EOF

echo 'Bye'

$SHELL