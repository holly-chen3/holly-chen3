#coding: utf-8
from socket import *
#using the socket module
def request(connectionSocket):
	sentence = connectionSocket.recv(1024)
	if sentence == b'':
		return
	requestedFileName = sentence.split()
	requestedName = requestedFileName[1][1:]
	print(f"{requestedName}\n")
	try: 
		requestedFile = open(requestedName, 'rb').read()
		connectionSocket.send(b'HTTP/1.1 200 OK\r\n')
		if 'html' in str(requestedName):
			connectionSocket.send(b'Content-Type: text/html \r\n')
		else: 
			connectionSocket.send(b'Content-Type: image/png \r\n')
		response = ("Content-Length: {}\r\n\r\n".format(len(requestedFile)))
		connectionSocket.send(response.encode('utf-8'))
		connectionSocket.send(requestedFile)
	except IOError:
		connectionSocket.send(b'HTTP/1.1 404 Not Found\r\n')
		errorMessage = b'<html><h2>404 Not Found<h2><html>'
		connectionSocket.send(b'Content-Type: text/html \r\n')
		errorResponse = ("Content-Length: {}\r\n\r\n".format(len(errorMessage)))
		connectionSocket.send(errorResponse.encode('utf-8'))
		connectionSocket.send(errorMessage)

serverPort = 12000 

serverSocket = socket(AF_INET, SOCK_STREAM)

serverSocket.bind(('localhost', serverPort))

serverSocket.listen(1)

print(f"The server is ready to receive on: http://127.0.0.1:{serverPort}")

while 1:
	connectionSocket, addr = serverSocket.accept()
	try: 
		while 1:
			request(connectionSocket)
	finally:
		connectionSocket.close()

