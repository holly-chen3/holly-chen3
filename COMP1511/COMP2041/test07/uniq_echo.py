#!usr/bin/env python3
import sys

wordSet = set()
for word in sys.argv[1:]:
	if word not in wordSet:
		print(word, end=' ')
		wordSet.add(word)
print()
