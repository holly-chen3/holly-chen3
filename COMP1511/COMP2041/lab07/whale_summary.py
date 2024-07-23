#!/usr/bin/env python3
import sys
import re
indDict = {}
podDict = {}
for file in sys.argv[1:]:
	with open(file, 'r') as outFile:
		lowerCaseLines = [line.lower() for line in outFile]
		for line in lowerCaseLines:
			line = line.strip()
			words = line.split()
			words = ' '.join(words).rstrip("s").split(maxsplit=2)
			if words[2] not in indDict:
				indDict[words[2]] = int(words[1])
				podDict[words[2]] = 1
			else:
				indDict[words[2]] += int(words[1])
				podDict[words[2]] += 1
for key in sorted(podDict):
	print(f"{key} observations: {podDict[key]} pods, {indDict[key]} individuals")
