#!/usr/bin/env python3
import sys
import re
from collections import Counter

def main():
	numList = [int(num) for num in sys.argv[1:]]
	count = len(numList)
	unique = len(set(numList))
	minimum = min(numList)
	maximum = max(numList)
	mean = findMean(numList, count)
	median = findMedian(sorted(numList))
	mode = findMode(numList)
	sumNums = sum(numList)
	productNums = productList(numList)
	print(f"count={count}")
	print(f"unique={unique}")
	print(f"minimum={minimum}")
	print(f"maximum={maximum}")
	print(f"mean={mean}")
	print(f"median={median}")
	print(f"mode={mode}")
	print(f"sum={sumNums}")
	print(f"product={productNums}")

def findMean(numList, count):
	if sum(numList) % count:
		return sum(numList) / count
	else:
		return sum(numList) // count
def findMedian(numList):
	lenNum = len(numList)
	middle = lenNum // 2
	return numList[middle]

def findMode(numList):
	countList = Counter(numList)
	highestCount = max(countList.values())
	for num, count in countList.items():
		if count == highestCount:
			return num

def productList(numList):
	product = 1
	for num in numList:
		product *= num
	return product

if __name__ == "__main__":
	main()
	
