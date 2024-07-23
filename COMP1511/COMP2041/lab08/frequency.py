#!/usr/bin/env python3
import sys
import re
from glob import glob

word = sys.argv[1]
for f in sorted(glob("lyrics/*.txt")):
	artist = " ".join(f.split("/")[1].split(".")[0].split("_"))
	with open(f, 'r') as outFile:
		count = 0
		lines = outFile.readlines()
		lenMatches = 0
		for line in lines:
			line = line.strip()
			matches = re.findall(r'[a-zA-Z]+', line)
			if matches:
				lenMatches += len(matches)
			for lineMatch in matches:
				if lineMatch.lower() == word:
					count += 1
		frequency = count / lenMatches
		print(f"{count:4}/{lenMatches:6} = {frequency:.9f} {artist}")
	
	
