#!/bin/dash

if [ "$#" -ne 2 ]
then
	echo "Usage: $0 <number of lines> <string>" 1>&2
	exit 1
fi
if [ "$1" -ge 0 ] 2>/dev/null
then
	:
else
	echo "$0: argument 1 must be a non-negative integer" 1>&2
	exit 1
fi
num="$1"
while [ "$num" -gt 0 ]
do
	echo "$2"
	num=$((num - 1))
done
exit 0

