#!/usr/bin/env python3
import sys

start = int(sys.argv[1])
end = int(sys.argv[2])
fileName = sys.argv[3]
with open(fileName, 'a') as inFile:
	while start <= end:
		inFile.write(str(start)+'\n')
		start += 1
