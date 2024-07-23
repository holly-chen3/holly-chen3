#!/usr/bin/env python3
import re
import sys
import bs4 as BeautifulSoup
import requests
def courses_requests():
	r = requests.get(f'http://www.timetable.unsw.edu.au/2024/{sys.argv[1]}KENS.html', auth=('user', 'pass'))
	soup = BeautifulSoup.BeautifulSoup(r.text, "html5lib")
	matches = []
	for element in soup.findAll("a", {"href": re.compile(sys.argv[1] + r'[0-9]{4}\.html')}, string=True):
		text = re.sub(r"\n\s+", "\n", element.text)
		text = text.strip()
		if text:
		    matches.append(text)
	res = sorted(matches[i] + " " + matches[i + 1] for i in range(0, len(matches), 2))
	uniqueRes = list(dict.fromkeys(res))
	print("\n".join(uniqueRes))
	
if __name__ == "__main__":
	courses_requests()
