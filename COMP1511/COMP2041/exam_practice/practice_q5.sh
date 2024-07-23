#!/bin/dash

regex=$1
filename=$2
years=$(grep -E "^$regex\|" $filename | cut -d"|" -f2)
firstOffered=$(echo "$years" | sort -n | sed 1q)
lastOffered=$(echo "$years" | sort -nr | sed 1q)
if [ -z "$years" ]
then
    echo "No award matching '$regex'" >&2
    exit 1
fi
for year in $(seq $firstOffered $lastOffered)
do
    if ! echo "$years" | grep -q "$year"
    then
        echo "$year"
    fi
done
exit 0