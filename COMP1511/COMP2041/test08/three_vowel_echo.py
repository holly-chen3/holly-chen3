#!/usr/bin/env python3
import sys
import re
vowels = ['a', 'e', 'i', 'o', 'u']
for word in sys.argv[1:]:
	count = 0
	for letter in word.lower():
		if letter in vowels:
			count += 1
		else:
			count = 0
		if count >= 3:
			print(word, end=" ")
			break
print()
