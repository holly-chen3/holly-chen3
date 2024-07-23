#!/usr/bin/env python3
import sys
import re
last_names = []
for student in sys.stdin:
    if re.search(r'M$', student):
        sections = re.split(r'\|', student)
        names = re.split(r'\,', sections[2])
        if names[0] not in last_names:
            last_names.append(names[0])
for last_name in sorted(last_names):
    print(last_name)
