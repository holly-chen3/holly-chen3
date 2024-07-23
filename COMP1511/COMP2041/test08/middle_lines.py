#!/usr/bin/env python3
import sys
import re

fileName = sys.argv[1]
with open(fileName, 'r') as outFile:
	lines = outFile.readlines()
	if len(lines) == 0:
		sys.exit(0)
	middle1 = len(lines) // 2
	if len(lines) % 2 == 0:
		middle2 = len(lines) // 2 - 1
		print(lines[middle2], end="")
	print(lines[middle1], end="")
