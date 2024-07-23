"""
    Python 3
    Usage: python3 server.py 12000 NUM_OF_CONSECUTIVE_TRIES
    coding: utf-8
    
    Author: Holly Chen (z5359932)
"""
from socket import *
from threading import Thread
import sys, select
from datetime import datetime
from os.path import exists
import os, glob

clientList = []
messageList = []
groupChatCreator = {}
groupChatMembers = {}
joinedMembers = {}
blockedUsers = {}

# Function to handle client connections
class ClientThread(Thread):
	def __init__(self, clientAddress, clientSocket):
		Thread.__init__(self)
		self.clientAddress = clientAddress
		self.clientSocket = clientSocket
		self.clientAlive = False
		self.clientLoginCounter = 1
		self.userName = ''
		print("===== New connection created for: ", clientAddress)
		self.clientAlive = True
        
	def run(self):
		message = ''
        
		while self.clientAlive:
			# use recv() to receive message from the client
			data = self.clientSocket.recv(1024)
			message = data.decode()
			# if the message from client is empty, the client would be off-line then set the client as offline (alive=Flase)
			if message == '':
				self.clientAlive = False
				print("===== the user disconnected - ", clientAddress)
				self.delete_client()
				break
            		# handle message from the client
			if message == 'login':
				self.print_message("New login request")
				self.process_login()
			elif 'login-details' in message:
				self.print_message("Login detail request")
				self.login_user(message.replace('login-details:', ''))
			elif 'udp-server-port' in message:
				self.print_message("Received UDP Server Port")
				self.udp_server_port(message.replace('udp-server-port:', ''))
			elif 'command' in message: 
				self.print_message(f"New Command request: {message}")
				self.pass_command(message.replace('command:', ''))
			else:
				message = 'Cannot understand this message'
				self.print_message(message)
				self.clientSocket.send(message.encode())

   
	"""
        API functions
	"""
	def print_message(self, message):
		print("> " + message)
	def process_login(self):
		message = 'user credentials request'
		self.print_message(message)
		self.clientSocket.send(message.encode())
	def too_many_tries(self):
        	self.print_message("Too many tries")
        	self.clientSocket.send('Too many tries. Try again in 10 seconds.'.encode())
	def login_user(self, credentials):
		self.userName = credentials.split(' ')[0]
		if self.blocked_user():
			self.too_many_tries()
			return
		if self.clientLoginCounter == numberOfConsecutiveAttempts:
			self.too_many_tries()
			blockedUsers[self.userName] = datetime.now()
			return
		with open('credentials.txt', 'r') as f:
			credUsers = f.readlines()
			message = 'user inputted invalid credentials'
			self.clientLoginCounter += 1
			for credUser in credUsers:
				if credUser.replace('\n', '') == credentials:
					message = "user logged in"
					self.clientLoginCounter -= 1
			self.print_message(message)
			self.clientSocket.send(message.encode())
			
		
	def blocked_user(self):
		if self.userName in blockedUsers:
			now = datetime.now()
			blockedtime = now - blockedUsers[self.userName]
			if blockedtime.total_seconds() > 10:
				blockedUsers.pop(self.userName)
				return False
			return True
		return False
	def pass_command(self, command):
		if '/msgto' in command: 
			self.msg_to(command)
		elif '/activeuser' in command:
			self.active_user()
		elif '/creategroup' in command:
			self.create_group(command)
		elif '/joingroup' in command:
			self.join_group(command)
		elif '/groupmsg' in command:
			self.group_msg(command)
		elif '/logout' in command:
			self.logout()
		else:
			message = 'error: invalid command selected'
			print('> ' + message)
			self.clientSocket.send(message.encode())
	def process_num(self, lines):
		if len(lines) > 0:
			lastLine = lines[-1]
			return int(lastLine.split('; ', 1)[0]) + 1
		else: 
			return 1
	def process_log(self, fileName, fileData, message, messageTimestamp):
		num = 1
		if exists(fileName):
			f = open(fileName, "r")
			lines = f.readlines()
			num = self.process_num(lines)
		f = open(fileName, "a")
		timestamp = datetime.now().strftime('%d %b %Y %H:%M:%S')
		f.write(f"{num}; {timestamp}; {fileData}\n")
		f.close()
		if messageTimestamp:
			message = message + f'{timestamp}'
		print('> ' + message)
		self.clientSocket.send(message.encode())
		return timestamp
	def msg_to(self, command):
		if command == '/msgto': 
			self.command_error()
			return
		command = command.replace('/msgto ', '')
		commandList = command.split(' ', 1)
		if any(client.get('username', None) == commandList[0] for client in clientList):
			self.send_message(commandList[0], commandList[1])
	def active_user(self):	
		lines = []
		with open("userlog.txt", "r") as f:
			lines = f.readlines()
		activeUsers = filter(lambda client: (self.userName not in client), lines)
		activeUserMessage = 'active users: \n'
		for user in activeUsers:
			activeUserMessage += (user[3:] + '\n')
		if activeUserMessage == 'active users: \n':
			message = "error: no other active user"
			self.print_message(message)
			self.clientSocket.send(message.encode())
		else:
			self.print_message(activeUserMessage)
			self.clientSocket.send(activeUserMessage.encode())
	def create_group(self, command):
		if command == '/creategroup': 
			self.command_error()
			return
		command = command.replace('/creategroup ', '')
		commandList = command.split(' ')
		groupName = commandList.pop(0)
		fileName = f"{groupName}_messagelog.txt"
		fileExists = exists("./" + fileName)
		if fileExists:
			self.clientSocket.send('error: already exists a group chat (Name: {groupName})'.encode())
			return
		for user in commandList:
			if not any(client['username'] == user for client in clientList):
				self.clientSocket.send('error: invalid or offline users'.encode())
				return
		with open(fileName, "a") as f:
			f.close()
		groupChatCreator[groupName] = self.userName
		joinedMembers[groupName] = self.userName
		groupChatMembers[groupName] = commandList
		message = f"Group chat room has been created, room name: {groupName}, users in this room:"
		for user in commandList:
			message += f" {user}"
		self.clientSocket.send(message.encode())
	def join_group(self, command):
		if command == '/joingroup':
			self.command_error()
			return
		groupName = command.replace('/joingroup ', '')
		if groupName not in groupChatCreator:
			self.clientSocket.send('error: not a valid group chat name'.encode())
			return
		if self.userName in joinedMembers[groupName]:
			return
		if self.userName not in groupChatMembers[groupName]:
			self.clientSocket.send('error: not invited to the group chat'.encode())
			return
		joinedMembers[groupName] = joinedMembers[groupName] + self.userName
		self.clientSocket.send(f"successfully joined {groupName}".encode())
	def group_msg(self, command):
		if command == '/groupmsg':
			self.command_error()
			return
		commandList = command.replace('/groupmsg ', '').split(' ', 1)
		groupName = commandList[0]
		message = commandList[1]
		if groupName not in groupChatCreator:
			self.clientSocket.send('error: not a valid group chat name'.encode())
			return
		if self.userName not in groupChatMembers[groupName] and self.userName not in groupChatCreator[groupName]:
			self.clientSocket.send('error: You are not in this group chat'.encode())
			return
		if self.userName not in joinedMembers[groupName]:
			self.clientSocket.send('error: You have not joined this group chat'.encode())
			return
		timestamp = self.process_log(f"{groupName}_messagelog.txt", f"{self.userName}; {message}", f"group message has been successfully sent in {groupName} ", True)
		receiverSockets = filter(lambda client: (client['username'] in joinedMembers[groupName] and client['username'] != self.userName), clientList)
		for receiverSocket in receiverSockets:
			recvMessage = f"Message Received! {timestamp}, {groupName}, {self.userName}: {message}"
			receiverSocket['clientSocket'].send(recvMessage.encode())
	def logout(self):
		self.clientSocket.send(f"closing connection with user {self.userName}".encode())
		self.delete_client()
	def command_error(self):
		message = 'error: no argument after command'
		print('> ' + message)
		self.clientSocket.send(message.encode())
	def send_message(self, receiver, message):
		print('send_message')
		timestamp = self.process_log('messagelog.txt', f'{receiver}; {message}', f'message was successfully sent at ', True)
		receiverSocket = list(filter(lambda client: (client['username'] == receiver), clientList))[0]
		receiverSocket['clientSocket'].send(f"Message Received! {timestamp}, {self.userName}: {message}".encode())
	def udp_server_port(self, port):
		ports = port.split('; ')
		udpPort = ports[0]
		clientHostName = ports[1].replace('clientIP: ', '')
		if not any(client.get('username', None) == self.userName for client in clientList):
			clientList.append({ 'username': self.userName, 'clientSocket': self.clientSocket })
			self.process_log('userlog.txt', f'{self.userName}; {clientHostName}; {udpPort}', 'command', False)
		else:
			message = "error: user already exists"
			print('> ' + message)
			self.clientSocket.send(message.encode())
	def delete_client(self):
		global clientList
		clientList = [client for client in clientList if not (client['username'] == self.userName)]
		with open("userlog.txt", "r") as f:
			lines = f.readlines()
			f.close()
		with open("userlog.txt", "w") as f:
			activeUserNum = 1
			for line in lines:
				if self.userName not in line:
					lineNum = line.split('; ', 1)[0]
					f.write(f"{activeUserNum}; " + line.replace(f"{lineNum}; ", ''))
					activeUserNum += 1
			
if __name__ == "__main__":
	# acquire server host and port from command line parameter
	if len(sys.argv) != 3:
	    print("\n===== Error usage, python3 TCPServer3.py SERVER_PORT NUMBER_OF_CONSECUTIVE_ATTEMPTS======\n")
	    exit(0)
	serverHost = "127.0.0.1"
	serverPort = int(sys.argv[1])
	serverAddress = (serverHost, serverPort)
	numberOfConsecutiveAttempts = int(sys.argv[2])
	if os.path.exists('userlog.txt'):
		os.remove('userlog.txt')
	elif os.path.exists('messagelog.txt'):
		os.remove('messagelog.txt')
	for filename in glob.glob("*messagelog.txt"):
	    os.remove(filename) 
	serverSocket = socket(AF_INET, SOCK_STREAM)
	serverSocket.bind(serverAddress)

	print("\n===== Server is running =====")
	print("===== Waiting for connection request from clients...=====")


	while True:
	    serverSocket.listen()
	    clientSocket, clientAddress = serverSocket.accept()
	    clientThread = ClientThread(clientAddress, clientSocket)
	    clientThread.start()
	serverSocket.close()
