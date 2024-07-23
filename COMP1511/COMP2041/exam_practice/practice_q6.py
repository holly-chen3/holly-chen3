#!/usr/bin/env python3
import sys
import re

def main():
    firstFile = sys.argv[1]
    secondFile = sys.argv[2]
    with open(firstFile, 'r') as firstOutFile:
        firstLines = firstOutFile.readlines()
    with open(secondFile, 'r') as secondOutFile:
        secondLines = secondOutFile.readlines()
    secondLines = secondLines[::-1]
    if firstLines == secondLines:
        print('Mirrored')
        return
    if len(firstLines) != len(secondLines):
        print(f"Not mirrored: different number of lines: {len(firstLines)} versus {len(secondLines)}")
        return 
    for i in range(len(firstLines)):
        if firstLines[i] != secondLines[i]:
            print(f"Not mirrored: line {i + 1} different")
            return 

if __name__ == "__main__":
    main()