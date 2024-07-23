#!/bin/dash

maxLines=0
maxFile=""
for fileName in "$@"
do
	numLines=$(( $( wc -l "$fileName" | sed -E 's/^([0-9]+).*/\1/g' ) ))
	if [ "$numLines" -gt "$maxLines" ]
	then
		maxLines="$numLines"
		maxFile="$fileName"
	fi
done
echo "$maxFile"
exit 0
