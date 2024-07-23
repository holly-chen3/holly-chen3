from time import time
#create a file
f = open("log2.txt", "r")

with open("log2.txt", "a") as f:
	for _in range(5):
	
		#input messages
		msg = input("Your msg: ")

		#add the timestamp
		current_time = time()

		#logging -> writing into the file
		f.write(f"{msg} {current_time}\n")

#close file
#f.close()

with open("log2.txt", "r") as f:
	Lines = f.readlines()
	for line in lines: 
		print(line + "\n")
