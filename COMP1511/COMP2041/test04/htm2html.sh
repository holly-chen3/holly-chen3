#!/bin/dash

for fileName in *.htm
do
	newFileName="$fileName"l
	if [ -f "$newFileName" ] 
	then
		echo "$newFileName exists"
		exit 1
	fi
	mv "$fileName" "$newFileName"
done
exit 0

