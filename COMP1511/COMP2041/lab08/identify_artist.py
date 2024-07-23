#!/usr/bin/env python3
import sys
import re
import math
from glob import glob

def log_probability(words):
	dictProb = {}
	for f in sorted(glob("lyrics/*.txt")):
		artist = " ".join(f.split("/")[1].split(".")[0].split("_"))
		with open(f, 'r') as outFile:
			lines = outFile.read().lower()
			matches = re.findall(r'[a-zA-Z]+', lines)
			lenMatches = len(matches)
			frequencies = []
			if lenMatches:
				for word in words:
					count = matches.count(word) + 1
					frequencies.append(math.log(count / lenMatches))
			logProbability = 0
			for frequency in frequencies:
				logProbability += frequency
			dictProb[artist] = logProbability
	return dictProb
	
def identify_artist():
	for filename in sys.argv[1:]:
		max_log = 0
		with open(filename, 'r') as outFile:
			text = outFile.read().lower()
			matches = re.findall(r'[a-zA-Z]+', text)
			if matches:
				dictProb = log_probability(matches)
				print(f"{filename} most resembles the work of {max(dictProb, key=dictProb.get)} (log-probability={max(dictProb.values()):.1f})")

if __name__ == "__main__":
	identify_artist()
			
		
			
