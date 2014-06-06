#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define REMOTE_SERVER_PORT 1500
#define MAX_MSG 100
#define SUCCESS 0
#define ERROR   1
#define END_LINE 0x0

//#define LOCAL_CLIENT_PORT 1500

/* function readline */
int read_line();

int main(int argc, char *argv[]) {
  
 	int sd, rc, i;
  	struct sockaddr_in cliAddr, remoteServAddr;
  	struct hostent *h;
	
//Checking Command Line errors.
  	if(argc<2) 
	{
    		printf("\nUsage : %s <server>\n", argv[0]);
    		exit(1);
  	}

//Validating Server IP Address.
  	h = gethostbyname(argv[1]);
	if(h==NULL) 
	{
    		printf("%s: Unknown Host '%s' \n", argv[0], argv[1]);
    		exit(1);
  	}

  	printf("%s: Connection is established with '%s' (IP : %s) \n", argv[0], h->h_name,
	inet_ntoa(*(struct in_addr *)h->h_addr_list[0]));

  	remoteServAddr.sin_family = h->h_addrtype;
  	memcpy((char *) &remoteServAddr.sin_addr.s_addr, h->h_addr_list[0], h->h_length);
  	remoteServAddr.sin_port = htons(REMOTE_SERVER_PORT);

//Creating the Socket.
  	sd = socket(AF_INET,SOCK_DGRAM,0);
  	if(sd<0) 
	{
    		printf("%s: Cannot open the Socket.\n",argv[0]);
    		exit(1);
	}
  
//Bind any PORT
  	cliAddr.sin_family = AF_INET;
  	cliAddr.sin_addr.s_addr = htonl(INADDR_ANY);
  	cliAddr.sin_port = htons(0);
  
  	rc = bind(sd, (struct sockaddr *) &cliAddr, sizeof(cliAddr));


  	if(rc<0) 
	{
    		printf("%s: Cannot bind Port\n", argv[0]);
    		exit(1);
  	}

//********************
//UDP on 1500
//********************

	char message[100];
	int serverSize;

	rc = sendto(sd, "cs11b003", 100, 0, (struct sockaddr *) &remoteServAddr, sizeof(remoteServAddr));

	if(rc<0) 
	{
		printf("\nFailed: Could not communicate with the SERVER.");
		close(sd);
		exit(1);
	}
	
	printf("\nMessage 'cs11b003' is sent to the SERVER (via UDP).");	


	rc = recvfrom(sd, message, 100, 0, (struct sockaddr *) &remoteServAddr, &serverSize);

	if(rc<0) 
	{
		printf("%s: Cannot receive data.", argv[0]);
	}

	printf("\nMessage '%s' is received from the SERVER.", message);

	int NEW_PORT = atoi(message);


//********************
//TCP on NEW Port
//********************

 	int sd2, rc2;	
	struct sockaddr_in localAddr2, servAddr2;
 	struct hostent *h2;
  	char line[100];

  	servAddr2.sin_family = h->h_addrtype;
  	memcpy((char *) &servAddr2.sin_addr.s_addr, h->h_addr_list[0], h->h_length);
  	servAddr2.sin_port = htons(NEW_PORT);

//Create socket
	close(sd);

  	sd2 = socket(AF_INET, SOCK_STREAM, 0);
  	if(sd2<0) {
    		perror("cannot open socket ");
    		exit(1);
  	}

//Bind any port number
  	localAddr2.sin_family = AF_INET;
  	localAddr2.sin_addr.s_addr = htonl(INADDR_ANY);
  	localAddr2.sin_port = htons(0);
  
  	rc = bind(sd2, (struct sockaddr *) &localAddr2, sizeof(localAddr2));
  	if(rc<0) {
    		printf("%s: cannot bind port TCP %u\n",argv[0],NEW_PORT);
    		perror("error ");
    		exit(1);
  	}
				
//Connect
  	rc = connect(sd2, (struct sockaddr *) &servAddr2, sizeof(servAddr2));
  	if(rc<0) {
    		perror("cannot connect ");
    		exit(1);
  	}

// Send NEW Message

	printf("\nNew PORT number %d is set for TCP Connection.", NEW_PORT);

    	rc = send(sd2, "cs11b003", 100, 0);
    
    	if(rc<0) {
      		perror("cannot send data ");
      		close(sd2);
      		exit(1);    
    	}
	printf("\nMessage 'cs11b003' is sent to the SERVER (via TCP).\n\n");	

   	memset(line,0x0,100);
    	
	while(read_line(sd2,line) != 1) {
	  	printf("\nReceived message: %s",line);
      		memset(line,0x0,100);
		break;
      	}

  	return 1;
}


int read_line(int newSd, char *line_to_return) {
  
  static int rcv_ptr=0;
  static char rcv_msg[MAX_MSG];
  static int n;
  int offset;

  offset=0;

  while(1) {
    if(rcv_ptr==0) {
      /* read data from socket */
      memset(rcv_msg,0x0,MAX_MSG); /* init buffer */
      n = recv(newSd, rcv_msg, MAX_MSG, 0); /* wait for data */
      if (n<0) {
	perror(" cannot receive data ");
	return ERROR;
      } else if (n==0) {
	printf(" connection closed by client\n");
	close(newSd);
	return ERROR;
      }
    }
  
    /* if new data read on socket */
    /* OR */
    /* if another line is still in buffer */

    /* copy line into 'line_to_return' */
    while(*(rcv_msg+rcv_ptr)!=END_LINE && rcv_ptr<n) {
      memcpy(line_to_return+offset,rcv_msg+rcv_ptr,1);
      offset++;
      rcv_ptr++;
    }
    
    /* end of line + end of buffer => return line */
    if(rcv_ptr==n-1) { 
      /* set last byte to END_LINE */
      *(line_to_return+offset)=END_LINE;
      rcv_ptr=0;
      return ++offset;
    } 
    
    /* end of line but still some data in buffer => return line */
    if(rcv_ptr <n-1) {
      /* set last byte to END_LINE */
      *(line_to_return+offset)=END_LINE;
      rcv_ptr++;
      return ++offset;
    }

    /* end of buffer but line is not ended => */
    /*  wait for more data to arrive on socket */
    if(rcv_ptr == n) {
      rcv_ptr = 0;
    } 
    
  } /* while */
}















