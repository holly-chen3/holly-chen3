#!/bin/dash

d1=$1
d2=$2
for file1 in "$d1"/*
do
	if diff "$file1" "$d2" >/dev/null 2>&1
	then
		fileName=$( basename $file1 )
		echo "$fileName"
	fi
done
