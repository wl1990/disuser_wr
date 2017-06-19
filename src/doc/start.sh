#!/bin/sh

rm -f tpid
nohup java -jar disuser.jar

echo $!>tpid

echo Start Success!