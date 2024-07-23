#!/usr/bin/env python3
import sys
import re

class oneComm:
	def __init__(self, command):
		self.cmdStr = re.sub(r'\s', '', command)
		self.delim = ""
		self.end = ""
		self.replaceStr = ""
		self.globalFlag = ""
		self.addrRegex = ""
		self.flag = ""
		self.start = ""
		self.comm = None
	
	def findOptions(self):
		self.findFlag()
		self.findDelim()
		self.findStartandEnd()
		if self.flag == "s":
			self.findAddrRegex()
		self.setUpComms()

	def findFlag(self):
		if m := re.search(r'([dqp])$', self.cmdStr):
			if re.match(r'^d$', self.cmdStr):
				sys.exit(0)
			self.flag = m.group(0)
		else:
			# if s in in the command string and is being called to substitute
			if m:= re.search(r'^[\d\$,]*s|^(\S){1}.+\1s', self.cmdStr):
				self.flag = 's'

	def findDelim(self):
		# if s is in the command string and it is at the start of the command
		command = self.cmdStr
		if self.flag == 's':
			self.delim = re.search(r's(\S)', self.cmdStr).group(1)
		else:
			self.delim = "/"

	def findStartandEnd(self):
		startofStr = re.sub(re.compile(self.flag + r'.*$'), '', self.cmdStr, 1)
		startEnd = list(filter(None, re.split(r',', startofStr)))
		if len(startEnd) >= 1: 
			self.cmdStr = re.sub(startEnd[0], '', self.cmdStr, 1)
		self.cmdStr = re.sub(r',', '', self.cmdStr, 1)
		if len(startEnd) == 2:
			self.start = startEnd[0]
			self.end = startEnd[1]
		elif len(startEnd) == 1:
			self.end = startEnd[0]
			self.start = ""
		elif len(startEnd) == 0:
			self.end = ""
			self.start = ""

	def findAddrRegex(self):
		if self.delim in self.cmdStr:
			self.cmdStr = re.sub(r'^.*s', 's', self.cmdStr, 1)
			m = re.split(re.escape(self.delim), self.cmdStr)
			i = 0
			while i < len(m) - 1:
				if '[' in m[i] and ']' not in m[i]:
					m[i] = m[i] + self.delim + m[i+1]
					del m[i+1]
				elif m[i].endswith('\\') and '\\\\' not in m[i]:
					m[i] = m[i] + m[i+1]
					del m[i+1]
				else:
					i += 1
			if len(m) > 4:
				print("eddy: command line: invalid command")
				exit(1)
			self.addrRegex = m[1]
			self.replaceStr = m[2]
			if len(m) == 4:
				self.globalFlag = m[3]
	
	def setUpComms(self):
		if self.flag == "q":
			self.comm = q(self.end, self.delim)
		elif self.flag == "p":
			self.comm = p(self.start, self.end, self.delim)
		elif self.flag == "d":
			self.comm = d(self.start, self.end, self.delim)
		elif self.flag == "s":
			self.comm = s(self.start, self.end, self.delim, self.addrRegex, self.replaceStr, self.globalFlag)

	def callComm(self, line, lineCount, lastLine, alrPrinted=False, noPrint=False):
		if self.flag == "q":
			tmpLine = self.comm.process(line, lineCount, lastLine, alrPrinted, noPrint)
		else:
			tmpLine = self.comm.process(line, lineCount, lastLine)
		return tmpLine
	
	def getFlag(self):
		return self.flag

class q:
	def __init__(self, end, delim):
		self.regexEnd = False
		self.end = end
		if delim in end:
			self.regexEnd = True
			self.end = re.sub(delim, '', end)
	
	def process(self, line, lineCount, lastLine, alrPrinted=False, noPrint=False):
		if self.end == "$":
			if lastLine:
				if noPrint:
					sys.exit(0)
				if not alrPrinted:
					print(line, end="")
				sys.exit(0)
			else:
				return line
		if self.regexEnd and re.search(self.end, line):
			if noPrint:
				sys.exit(0)
			if not alrPrinted:
				print(line, end="")
			sys.exit(0)
		elif not self.regexEnd and lineCount == int(self.end):
			if noPrint:
				sys.exit(0)
			if not alrPrinted:
				print(line, end="")
			sys.exit(0)
		return line
	
class p:
	def __init__(self, start, end, delim):
		self.regexStart = False
		self.regexEnd = False
		self.start = start
		self.end = end
		self.inRange = False
		if delim in start:
			self.regexStart = True
			self.start = re.sub(delim, '', start)
		if delim in end:
			self.regexEnd = True
			self.end = re.sub(delim, '', end)

	def process(self, line, lineCount, lastLine):
		if self.end == "$":
			if lastLine:
				return True
			else:
				return False
		if self.start == "" and self.end == "":
			return True

		if not self.inRange:
			if self.start != "":
				if self.regexStart and re.search(self.start, line):
					if not self.regexEnd and lineCount > int(self.end):
						self.inRange = False
						return True
					self.inRange = True
					return True
				elif not self.regexStart and lineCount == int(self.start):
					if not self.regexEnd and lineCount > int(self.end):
						self.inRange = False
						return True
					self.inRange = True
					return True
		else:
			if self.regexEnd and re.search(self.end, line):
				self.inRange = False
			elif not self.regexEnd and lineCount == int(self.end):
				self.inRange = False
			return True
		if not self.inRange and self.start == "":
			if self.regexEnd and re.search(self.end, line):
				return True
			elif not self.regexEnd and lineCount == int(self.end):
				return True
		return False

class d:
	def __init__(self, start, end, delim):
		self.regexStart = False
		self.regexEnd = False
		self.start = start
		self.end = end
		self.inRange = False
		if delim in start:
			self.regexStart = True
			self.start = re.sub(delim, '', start)
		if delim in end:
			self.regexEnd = True
			self.end = re.sub(delim, '', end)
		
	def process(self, line, lineCount, lastLine):
		if self.end == "$":
			if lastLine:
				return None
			else:
				return line
		if not self.inRange:
			if self.start != "":
				if self.regexStart and re.search(self.start, line):
					self.inRange = True
					return None
				elif not self.regexStart and lineCount == int(self.start):
					self.inRange = True
					return None
		else:
			if self.regexEnd and re.search(self.end, line):
				self.inRange = False
			elif not self.regexEnd and lineCount == int(self.end):
				self.inRange = False
			return None
		if not self.inRange and self.start == "":
			if self.regexEnd and re.search(self.end, line):
				return None
			elif not self.regexEnd and lineCount == int(self.end):
				return None
		return line

class s:
	def __init__(self, start, end, delim, addrRegex, replaceStr, globalFlag):
		self.regexStart = False
		self.regexEnd = False
		self.start = start
		self.end = end
		self.inRange = False
		if delim in start:
			self.regexStart = True
			self.start = re.sub(delim, '', start)
		if delim in end:
			self.regexEnd = True
			self.end = re.sub(delim, '', end)
		self.addrRegex = addrRegex
		self.replaceStr = replaceStr
		self.globalFlag = globalFlag

	def sub(self, line):
		tmpLine = line
		if self.globalFlag == "":
			tmpLine = re.sub(self.addrRegex, self.replaceStr, line, 1)
		else:
			tmpLine = re.sub(self.addrRegex, self.replaceStr, line)
		return tmpLine

	def process(self, line, lineCount, lastLine):
		if self.end == "$":
			if lastLine:
				return self.sub(line)
			else:
				return line
		if self.start == "" and self.end == "":
			return self.sub(line)
		tmpLine = line
		if not self.inRange:
			if self.start != "":
				if self.regexStart and re.search(self.start, line):
					self.inRange = True
					tmpLine = self.sub(line)
				elif not self.regexStart and lineCount == int(self.start):
					self.inRange = True
					tmpLine = self.sub(line)
		else:
			if self.regexEnd and re.search(self.end, line):
				self.inRange = False
			elif not self.regexEnd and lineCount == int(self.end):
				self.inRange = False
			tmpLine = self.sub(line)
		if not self.inRange and self.start == "":
			if self.regexEnd and re.search(self.end, line):
				tmpLine = self.sub(line)
			elif not self.regexEnd and lineCount == int(self.end):
				tmpLine = self.sub(line)
		return tmpLine
