#!/usr/bin/env python3
import sys
import re
import argparse
from helper import oneComm
def main():
	parser = setup_parser()
	args = parser.parse_args()
	command = args.cmd
	commandList = []
	inputFiles = args.inputFiles
	commands = []
	if command:
		if args.no_print and 'p' not in command:
			sys.exit(0)
		elif 'd' == command:
			sys.exit(0)
		command = re.sub(r'\s', '', command)
		commands = re.split(r'[;\n]', command)
	elif args.file:
		inputFiles = args.file[1:]
		commands = []
		with open(args.file[0], 'r') as outFile:
			lines = outFile.readlines()
			for line in lines:
				cmds = list(filter(None, re.split(r'[;\n]', line)))
				commands += cmds
	else:
		print("usage: eddy [-i] [-n] [-f <script-file> | <sed-command>] [<files>...]")
		sys.exit(0)
	commandPrint(args, command, commands, commandList, inputFiles)

def commandPrint(args, command, commands, commandList, inputFiles):
	for comm in commands:
		if hashComment := re.search(r'#.*$', comm):
			comm = re.sub(r'#.*$', '', comm).strip()
		if comm != "":
			newComm = oneComm(comm)
			newComm.findOptions()
			commandList.append(newComm)
	count = 0
	for inputFile in inputFiles:
		with open(inputFile, 'r') as outFile:
			lines = outFile.readlines()
			count = printNums(lines, commandList, commands, count, args.no_print)
	count = printNums(sys.stdin, commandList, commands, count, args.no_print)

def printNums(numList, commandList, commands, count, noPrint):
	iterator = iter(numList)
	currLine = next(iterator, None)
	lastLine = False
	hasSub = any(cmd.getFlag() == "s" for cmd in commandList)
	hasPrint = any(cmd.getFlag() == "p" for cmd in commandList)
	while currLine is not None:
		nextLine = next(iterator, None)
		if nextLine is None:
			lastLine = True
		count += 1
		tmpLine = currLine
		alrPrinted = False
		repeatLine = False
		containsP = False
		for cmd in commandList:
			if cmd.getFlag() == "p" and not alrPrinted and not noPrint and not hasSub:
				print(tmpLine, end="")
				alrPrinted = True
			if cmd.getFlag() == "p":
				containsP = True
				if not repeatLine:
					repeatLine = cmd.callComm(tmpLine, count, lastLine)
				if noPrint and not alrPrinted and repeatLine:
					print(tmpLine, end="")
					alrPrinted = True
			else:
				if cmd.getFlag() == "q" and not repeatLine:
					tmpLine = cmd.callComm(tmpLine, count, lastLine, alrPrinted, noPrint)
				elif cmd.getFlag() == "q" and repeatLine:
					if noPrint:
						tmpLine = cmd.callComm(tmpLine, count, lastLine, alrPrinted, noPrint)
					else:
						tmpLine = cmd.callComm(tmpLine, count, lastLine, False, noPrint)
				elif not alrPrinted and repeatLine and (cmd.getFlag() == "s" or cmd.getFlag() == "q"):
					print(tmpLine, end="")
					tmpLine = cmd.callComm(tmpLine, count, lastLine)
					alrPrinted = True
				else:
					tmpLine = cmd.callComm(tmpLine, count, lastLine)
			if tmpLine == None:
				break
		if tmpLine != None and not noPrint: 
			if containsP and repeatLine:
				if not alrPrinted:
					print(tmpLine, end="")
				print(tmpLine, end="")
			elif not containsP:
				print(tmpLine, end="")
			if hasSub and hasPrint and not repeatLine:
				print(tmpLine, end="")
		currLine = nextLine
	return count

def setup_parser():
	parser = argparse.ArgumentParser(description ='eddy main')
	parser.add_argument( "--no_print", "-n", action="store_true" )
	parser.add_argument( "cmd", nargs="?" )
	parser.add_argument( "--file", "-f", nargs="*" )
	parser.add_argument( "inputFiles", nargs="*" )
	return parser

if __name__ == "__main__":
	main()
