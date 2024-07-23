#!/bin/dash

if [ $# -eq 3 ]
then
	start=$1
	end=$2
	fileName=$3
	while [ "$start" -le "$end" ]
	do
		echo "$start" >> "$fileName"
		start=$(( start + 1 ))
	done
fi


