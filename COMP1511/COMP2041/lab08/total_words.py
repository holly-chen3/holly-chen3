#!/usr/bin/env python3
import sys
import re
def total_words():
	count = 0
	for line in sys.stdin:
		line = line.strip()
		matches = re.findall(r'[a-zA-Z]+', line)
		if matches:
			count += len(matches)
	print(f"{count} words")
	
if __name__ == "__main__":
	total_words()
