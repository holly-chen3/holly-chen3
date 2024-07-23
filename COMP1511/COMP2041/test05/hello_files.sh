#!/bin/dash

posInt=$1
stringName=$2
count=1
while [ "$count" -le "$posInt" ]
do
	echo "hello $stringName" > "hello$count.txt"
	count=$(( count + 1 ))
done
