#!/bin/dash

for filename in "$@"
do 
	display "$filename"
	echo -n "Address to e-mail this image to? "
	read -r email
	if test -z "$email"
	then
		echo "No email sent" 1>&2
		exit 1
	fi
	echo -n "Message to accompany image? "
	read -r message
	subject=$( echo $filename | sed 's/(.png|.jpg)//g' )
	echo "$message" | mutt -s "$subject!" -e 'set copy=no' -a "$filename" -- "$email"
	echo "$filename sent to $email"
done
exit 0

