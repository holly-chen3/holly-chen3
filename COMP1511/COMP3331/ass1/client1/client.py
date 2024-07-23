"""
    Python 3
    Usage: python3 Client.py localhost 12000 UDP-PORT
    coding: utf-8
    
    Author: Holly Chen (z5359932)
"""
from socket import *
import sys
import threading
import time
receivedMessages = []
activeUsersDict = {}
filePackets = []
fileDict = {}
def p2pServerConnection(udpServerPort):
	while True:
		clientConnected = True
		fileName = ''
		while clientConnected:
			message, clientAddress = serverSocket.recvfrom(2048)
			#receive data from the client, now we know who we are talking with
			message = message.decode('utf-8')
			if message == 'End of File':
				clientConnected = False
				break
			messageList = message.split(';', 2)
			fileName = messageList[0]
			seqNum = int(messageList[1])
			fileData = messageList[2].encode('latin-1')
			fileDict[seqNum] = fileData
		sortedFileDict = dict(sorted(fileDict.items()))
		data = bytearray()
		for seqNum in sortedFileDict:
			data.extend(sortedFileDict[seqNum])
		if fileName == '':
			return
		with open(fileName, "wb") as f:
			f.write(data)
		print('> Received file!')
		print('> Enter one of the following commands (/msgto, /activeuser, /creategroup, /joingroup, /groupmsg, /p2pvideo ,/logout): \n')
	serverSocket.close()
def loginCreds(clientSocket):
	username = input("Username: ")
	password = input("Password: ")
	print(f"> You have entered Username: {username} and Password: {password}")
	clientSocket.send(f"login-details:{username} {password}".encode())
def activeUsers(message):
	users = message.split('\n')
	for user in users:
		if (user == ''):
			break
		info = user.split('; ')
		userName = info[1]
		serverName = info[2]
		udpServerPort = info[3]
		activeUsersDict[userName] = {'servername': serverName, 'serverport': udpServerPort}
def p2pClientConnection(info):
	infoList = info.split(' ')
	AudienceUser = infoList[0]
	fileName = infoList[1]
	if AudienceUser not in activeUsersDict:
		print("> Error: not an active user")
		return
	else:
		serverName = activeUsersDict[AudienceUser]['servername']
		serverPort = int(activeUsersDict[AudienceUser]['serverport'])
		udpClientSocket = socket(AF_INET, SOCK_DGRAM)
		with open(fileName, "rb") as f:
			seqNum = 1
			while True:
				content = f.read(950).decode('latin-1').encode('utf-8')
				if content:
					header = (AudienceUser + '_' + fileName + ';' + "{0:03}".format(seqNum) + ';').encode('utf-8')
					packet = header + content
					filePackets.append(packet)
					seqNum += 1
				else:
					filePackets.append(f'End of File'.encode('utf-8'))
					break
		for packet in filePackets:
			time.sleep(0.0005)
			udpClientSocket.sendto(packet, (serverName, serverPort))
		udpClientSocket.close()
def commandRequest(clientSocket):
	command = input("> Enter one of the following commands (/msgto, /activeuser, /creategroup, /joingroup, /groupmsg, /p2pvideo ,/logout): \n")
	if '/p2pvideo' in command:
		p2pClientConnection(command.replace('/p2pvideo ', ''))
		commandRequest(clientSocket)
	else:
		clientSocket.send(f"command:{command}".encode())
def receiveMessages(clientSocket):
	global socketAlive
	while socketAlive:
		try:
			msg = clientSocket.recv(1024).decode()
		except:
			continue
		if msg == '':
			break
		if 'Message Received!' in msg:
			print("> " + msg.replace('Message Received! ', ''))
			print("> Enter one of the following commands (/msgto, /activeuser, /creategroup, /joingroup, /groupmsg, /p2pvideo ,/logout): \n")
		else:
			receivedMessages.append(msg)
def messageConnection(clientSocket):
	global socketAlive
	while socketAlive:
		if len(receivedMessages) > 0:
			receivedMessage = receivedMessages.pop(0)
			# parse the message received from server and take corresponding action
			if receivedMessage == "user logged in":
				print("> Welcome!")
				clientSocket.send(f"udp-server-port:{udpServerPort}; clientIP: {serverHost}".encode())
				continue
			elif receivedMessage == 'user inputted invalid credentials':
				print("> Failed Log in. Try again.")
				loginCreds(clientSocket)
				continue
			elif receivedMessage == "Too many tries. Try again in 10 seconds.":
				print("> Blocked from logging in. Try again later")
				socketAlive = False
				continue
			elif receivedMessage == "command" or receivedMessage == "no argument after command":
				print("> input another command")
			elif 'message was successfully sent at' in receivedMessage:
				print(f"> {receivedMessage}")
			elif 'active users:' in receivedMessage:
				print("> Active users found")
				print(receivedMessage)
				activeUsers(receivedMessage.replace('active users: \n', ''))
			elif 'error' in receivedMessage:
				print("> " + receivedMessage)
			elif 'Group chat room has been created' in receivedMessage:
				print("> Group chat room has been created")
			elif 'successfully joined' in receivedMessage:
				print("> You have successfully joined")
			elif 'group message has been successfully sent in' in receivedMessage:
				print("> You have successfully sent group message")
			elif 'closing connection with user' in receivedMessage:
				user = receivedMessage.replace('closing connection with user ', '')
				print(f"> Bye, {user}!")
				socketAlive = False
				continue
			else:
				print("> Message makes no sense")
			commandRequest(clientSocket)
if __name__ == "__main__":
	if len(sys.argv) != 4:
	    print("\n===== Error usage, python3 TCPClient3.py SERVER_IP SERVER_PORT UDP_PORT ======\n")
	    exit(0)
	serverHost = sys.argv[1]
	serverPort = int(sys.argv[2])
	serverAddress = (serverHost, serverPort)
	udpServerPort = int(sys.argv[3])
	# define a socket for the client side, it would be used to communicate with the server
	clientSocket = socket(AF_INET, SOCK_STREAM)
	# build connection with the server and send message to it
	clientSocket.connect(serverAddress)
	socketAlive = True
	# initialised udp server
	serverSocket = socket(AF_INET, SOCK_DGRAM)
	serverSocket.bind(('localhost', udpServerPort))
	# login
	print("===== Please type input login details: =====\n")
	loginCreds(clientSocket)
	# receive messages
	clientSocket.settimeout(1)
	receiveMessagesThread = threading.Thread(target=receiveMessages, args=(clientSocket, ))
	receiveMessagesThread.start()
	p2pServerConnectionThread = threading.Thread(daemon=True, target=p2pServerConnection, args=(udpServerPort, ))
	p2pServerConnectionThread.start()
	messageConnection(clientSocket)
	clientSocket.close()
