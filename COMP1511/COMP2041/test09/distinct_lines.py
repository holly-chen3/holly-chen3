#!/usr/bin/env python3
import sys
import re

numDiff = sys.argv[1]
numList = []
count = 0
for line in sys.stdin:
	line = re.sub(r'\s+', '', line.lower())
	count += 1
	if line not in numList:
		numList.append(line)
	if len(numList) == int(numDiff):
		break
if len(numList) == int(numDiff):
	print(f"{len(numList)} distinct lines seen after {count} lines read.")
else:
	print(f"End of input reached after {count} lines read - {numDiff} different lines not seen.")
		
