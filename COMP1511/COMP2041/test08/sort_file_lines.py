#!/usr/bin/env python3
import sys
import re

fileName = sys.argv[1]
with open(fileName, 'r') as outFile:
	lines = outFile.readlines()
	lines.sort()
	lines.sort(key=len)
	for line in lines:
		print(line, end="")
