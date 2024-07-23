#!/bin/dash

filename=$1
seq "$(sort -n $filename | sed 1q)" "$(sort -nr $filename | sed 1q)" | grep -Exvf "$filename"