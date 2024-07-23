#!/bin/dash

for fileName in "$@"
do
	files=$(grep -E '#include "[^"]*"' "$fileName" | sed -E 's/#include "([^"]*)"/\1/g')
	for fileH in $files
	do
		if [ ! -e "$fileH" ]
		then
			echo "$fileH included into $fileName does not exist"
		fi
	done
done
