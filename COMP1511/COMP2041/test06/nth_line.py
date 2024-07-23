#!/usr/bin/env python3
import sys

nthLine = int(sys.argv[1])
fileName = sys.argv[2]
with open(fileName, 'r') as outFile:
	lines = outFile.readlines()
	for i in range(len(lines)):
		if i+1 == nthLine:
			print(lines[i], end="")
		
