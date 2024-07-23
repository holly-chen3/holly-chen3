#!/bin/dash

sort -t"|" -n -k2 | cut -d"|" -f2,3 | uniq | sed -E 's/.*, ([^ ]*) .*/\1/' | sort
