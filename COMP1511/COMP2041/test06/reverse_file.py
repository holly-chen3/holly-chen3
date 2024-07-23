#!/usr/bin/env python3
import sys

oldFile = sys.argv[1]
newFile = sys.argv[2]
reverselines = []
with open(oldFile, 'r') as outFile:
	lines = outFile.readlines()
	reverselines = lines[::-1]

with open(newFile, 'w') as inFile:
	for line in reverselines:
		inFile.write(line)
