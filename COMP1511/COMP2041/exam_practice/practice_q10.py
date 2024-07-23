#!/usr/bin/env python3

import sys
import re

for line in sys.stdin:
    deleteList = []
    line = re.split(r'\s', line)
    line = [item for item in line if item != ""]
    for word in line:
        letters = {}
        for letter in word:
            letter = letter.lower()
            if letter not in letters.keys():
                letters[letter] = 1
            else:
                letters[letter] += 1
        same = len(set(letters.values()))
        if same > 1:
            deleteList.append(word)
    for word in deleteList:
        if word in line:
            line.remove(word)
    line = " ".join(line)
    if not re.search(r'\n$', line):
        line += '\n'
    print(line, end="")
