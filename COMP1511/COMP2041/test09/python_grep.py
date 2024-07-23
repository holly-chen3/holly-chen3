#!/usr/bin/env python3
import sys
import re

regexNum = sys.argv[1]
regexFile = sys.argv[2]

with open(regexFile, 'r') as outFile:
	lines = outFile.readlines()
	for line in lines:
		if m:= re.search(regexNum, line):
			print(line, end="")
