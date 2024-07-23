#!/usr/bin/env python3
import sys
import re

fileName = sys.argv[1]
sumNums = 0
with open (fileName, 'r') as outFile:
	for line in outFile:
		findNum = re.findall(r'\d+', line)
		for num in findNum:
			sumNums += int(num)
print(sumNums)
