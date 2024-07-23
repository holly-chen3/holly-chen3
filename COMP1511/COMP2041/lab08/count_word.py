#!/usr/bin/env python3
import sys
import re
def count_word():
	word = sys.argv[1]
	count = 0
	for line in sys.stdin:
		line = line.strip()
		matches = re.findall(r'[a-zA-Z]+', line)
		for match in matches:
			if match.lower() == word:
				count += 1
	print(f"{word} occurred {count} times")
	
if __name__ == "__main__":
	count_word()
