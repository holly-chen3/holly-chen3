#!usr/bin/env python3
import sys

num = int(sys.argv[1])
wordDict = {}
for word in sys.stdin:
	if word not in wordDict:
		wordDict[word] = 1
	else:
		wordDict[word] += 1
	if wordDict[word] == num:
		print(f"Snap: {word}", end="")
		break

