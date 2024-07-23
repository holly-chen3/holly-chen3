#!/bin/dash

num=0
while [ -d ".snapshot.$num" ]
do
	num=$((num + 1))
done
mkdir .snapshot.$num
echo "Creating snapshot $num"
for filename in *
do
	if [ -f "$filename" ] && [ "$filename" != "snapshot-save.sh" ] && [ "$filename" != "snapshot-load.sh" ]
	then
		cp -r "$filename" ".snapshot.$num"
	fi
done
exit 0
