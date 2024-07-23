#!/bin/dash

for filename in "$@"
do
	datetime=$( ls -l "$filename" | cut -d' ' -f6,7,8 )
	convert -gravity south -pointsize 36 -draw "text 0,10 \"$datetime\"" "$filename" "$filename"
done
