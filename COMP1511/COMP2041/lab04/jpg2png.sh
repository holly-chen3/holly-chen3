#!/bin/dash

for filename in *
do
	new_filename=$(
		echo "$filename" | 
		sed -E 's/.jpg$/.png/g'
	)
	[ "$new_filename" = '' ] && continue
	[ "$filename" = "$new_filename" ] && continue
	if test -e "$new_filename"
	then
		echo "$new_filename already exists" 1>&2
		exit 1
	fi
	convert "$filename" "$new_filename" >/dev/null
	rm "$filename"
done
exit 0

