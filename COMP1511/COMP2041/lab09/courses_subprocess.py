#!/usr/bin/env python3
import sys
import re
import subprocess

def courses_subprocess():
	data = subprocess.run(['curl', '--location', '--silent', f"http://www.timetable.unsw.edu.au/2024/{sys.argv[1]}KENS.html"], 
				stdout=subprocess.PIPE,
				stderr=subprocess.PIPE,
				text=True)
	matches = re.findall(r'.*<td class=\"data\"><a href=\"' + sys.argv[1] + r'[0-9]{4}.html\">.*', data.stdout)
	subMatches = [re.sub(r'.*<td class=\"data\"><a href=\"' + sys.argv[1] + r'[0-9]{4}.html\">(.*)<\/a>.*', r'\1', line) for line in matches]
	res = sorted(subMatches[i] + " " + subMatches[i + 1] for i in range(0, len(subMatches), 2))
	uniqueRes = list(dict.fromkeys(res))
	print("\n".join(uniqueRes))
	
if __name__ == "__main__":
	courses_subprocess()
