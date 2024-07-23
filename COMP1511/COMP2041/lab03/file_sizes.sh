#!/bin/dash

sFile=""
mFile=""
lFile=""

for filename in *
do 
	num=$( wc -l < "$filename" )
	if [ "$num" -lt 10 ]
	then
		sFile="$sFile $filename"
	elif [ "$num" -ge 10 ] && [ "$num" -lt 100 ]
	then 
		mFile="$mFile $filename"
	elif [ "$num" -ge 100 ]
	then
		lFile="$lFile $filename"
	fi
done

echo "Small files:$sFile"
echo "Medium-sized files:$mFile"
echo "Large files:$lFile"


