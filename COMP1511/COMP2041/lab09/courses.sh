#!/bin/dash

comm=$1
curl --location --silent "http://www.timetable.unsw.edu.au/2024/$1KENS.html" |
sed -En "s/.*<td class=\"data\"><a href=\"$comm[0-9]{4}.html\">(.*)<\/a>.*/\1/gp" | 
tr "\n" "%" |
sed -E "s/($comm[0-9]{4})%/\1 /g;s/%/\n/g" |
sort | uniq -w8
