#!/bin/dash

snapshot-save.sh
num=$1
for filename in ".snapshot.$num"/*
do
	cp -r "$filename" "."
done
echo "Restoring snapshot $num"
exit 0
