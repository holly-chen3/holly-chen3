#!/bin/dash

cut -d"|" -f2 | sort -n | uniq -c | grep -E '^\s*2\s' | sed -E 's/^.*\s//'
