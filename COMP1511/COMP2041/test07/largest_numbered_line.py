#!/usr/bin/env python3
import sys
import re

maxDict = {}
for line in sys.stdin:
	nums = re.findall(r'-?\d+\.?\d*', line)
	if nums:
		numsList = [eval(i) for i in nums]
		maxDict[line] = max(numsList)

if maxDict:
	overallMax = max(maxDict.values())
	print("".join(line for line, num in maxDict.items() if num == overallMax), end="")
	
	
		
