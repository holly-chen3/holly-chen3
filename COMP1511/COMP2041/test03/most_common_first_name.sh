#!/bin/dash

cut -d"|" -f3 | sed -E 's/.*, ([^ ]*) .*/\1/' | sort | uniq -c | sort -n | tail -n1 | sed -E 's/[^A-Za-z]*//'
