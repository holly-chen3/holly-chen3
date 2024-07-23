from socket import *
import sys
import random
import time
import numpy

serverName = '127.0.0.1'
serverPort = 12000

clientSocket = socket(AF_INET, SOCK_DGRAM)
pingCount = 1
seqNum = random.randrange(10000,20000)
listRTT = []
while(pingCount <= 20):
	messageTime = int(round(time.time() * 1000))
	message = 'PING ' + str(seqNum) + ' ' + str(messageTime) + ' \r\n'
	clientSocket.sendto(message.encode('utf-8'),(serverName, serverPort))
	clientSocket.settimeout(0.6)
	try: 
		modifiedMessage, serverAddress = clientSocket.recvfrom(2048)
		newTime = int(round(time.time() * 1000))
		RTT = newTime - messageTime
		print(f'ping to {serverName}, seq = {seqNum}, rtt = {int(RTT)} ms')
		listRTT.append(RTT)
		pingCount += 1 
		seqNum += 1
	except timeout: 
		print(f'ping to {serverName}, seq = {seqNum}, time out')
		pingCount += 1
		seqNum += 1
if listRTT != []: 
	print(f'minimum RTT: {int(min(listRTT))} ms, maximum RTT: {int(max(listRTT))} ms, average RTT: {int(numpy.average(listRTT))} ms')
clientSocket.close()


