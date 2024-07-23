#!/usr/bin/env python3
import sys
sum = 0
for file in sys.argv[1:]:
	with open(file, 'r') as outFile:
		lines = outFile.readlines()
		for line in lines:
			if "Orca" in line:
				words = line.split()
				sum += int(words[1])
print(f"{sum} Orcas reported")

