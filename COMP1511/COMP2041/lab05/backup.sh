#!/bin/dash

num=0
filename=$1
new_file=".$1.$num"
while [ -f "$new_file" ]
do
	num=$((num + 1))
	new_file=".$filename.$num"
done
cp "$filename" "$new_file"
echo "Backup of '$filename' saved as '$new_file'"
exit 0
