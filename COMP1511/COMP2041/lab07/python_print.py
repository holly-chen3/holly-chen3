#!/usr/bin/env python3
import sys
import re

commandLine = sys.argv[1]
print(commandLine)
words = commandLine.split()
print(words)
if '|' in commandLine:
	print(words[0])
else:
	with open(words[2], 'w') as outFile:
		outFile.write(words[0])


