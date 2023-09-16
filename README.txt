# Basic Chat program

This project consists mainly of;
- "Chat.java", JAVA source file for the chat program

# Description
The ChatProgram is a simple Java program that allows two users to send text messages and files to each other through a network connection. The program uses a client-server model, where one user acts as the server and the other as the client.

# Interface Commands
This is a really basic client utility which can perform 2 different functionalities;

- transfer <filename>, puts the files from the client to the server. The file transferred to the server will have an appended format of “new”, similar to get.


## How to use

To use the Chat, follow these steps:

1. Compile the program by running the following command in the terminal: `javac Chat.java`

2. Start the server by running the following command in the terminal: `java Chat`

3. Start the client by running the following command in another terminal: `java Chat <username>`

   Replace `<username>` with a unique name for the client.

4. When prompted, enter the port number that the client should connect to.

5. Type messages or file transfer commands in the client's terminal. Messages will be displayed in both the client's and server's terminals. Files will be transferred between the two computers.

## File transfer

To transfer a file, type the following command in the client's terminal: `transfer <filename>`

Replace `<filename>` with the name of the file you want to transfer. The file must be in the same directory as the program.

When the server receives the transfer command, it will send a message to acknowledge the transfer. Then, it will send the file to the client. The client will save the file as `new<filename>` in the same directory as the program.