#!/usr/bin/env python3
import sys
import re

def main():
    with open(fileName, 'r') as outFile:
        lines = outFile.readlines()
        newLines = []
        for line in lines:
            if len(line) <= posInt + 1:
                newLines.append(line)
                continue
            elif ' ' not in line:
                newLines.append(line)
                continue
            elif len(line) > posInt + 1:
                if ' ' in line[:posInt]:
                    lastSpaceIndex = line[:posInt].rfind(' ')
                    line = line[:lastSpaceIndex] + '\n' + line[lastSpaceIndex + 1:]
                else:
                    firstSpaceIndex = line.find(' ')
                    line = line[:firstSpaceIndex] + '\n' + line[firstSpaceIndex + 1:]
            newLines.append(line)
        if newLines == lines:
            return
    with open(fileName, 'w') as inFile:
        for line in newLines:
            inFile.write(line)
    main()

if __name__ == "__main__":
    posInt = int(sys.argv[1])
    fileName = sys.argv[2]
    main()