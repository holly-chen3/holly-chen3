#!/bin/dash

for dir in "$@"
do
	if [ -d "$dir" ]
	then
		./tag_music.sh "$dir"/*
	else
		filename=$(echo "$dir" | sed -E 's/.*\///')
		album=$(echo "$dir" | cut -d'/' -f2)
		title=$(echo "$filename" | cut -d'-' -f2 | sed -E 's/^ //;s/ $//')
		track=$(echo "$filename" | cut -d'-' -f1 | sed -E 's/^ //;s/ $//')
		artist=$(echo "$filename" | cut -d'-' -f3 | sed -E 's/.mp3$//g' | sed -E 's/^ //')
		year=$(echo "$album" | cut -d',' -f2 | sed -E 's/^ //')
		id3 -t"$title" "$dir" >/dev/null
		id3 -T"$track" "$dir" >/dev/null
		id3 -a"$artist" "$dir" >/dev/null
		id3 -A"$album" "$dir" >/dev/null
		id3 -y"$year" "$dir" >/dev/null
	fi
done
exit 0


