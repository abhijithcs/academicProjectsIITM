#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define REMOTE_SERVER_PORT 8000
#define LOCAL_CLIENT_PORT 8500

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
//CLIENT USER COMMANDS
//********************
	char userCommand[50];
	char message[100];
	FILE *file_temp;
	int serverSize;

	while(1)
	{
		printf("\nEnter a Command : ");
		gets(userCommand);
		
		rc = sendto(sd, userCommand, strlen(userCommand)+1, 0, (struct sockaddr *) &remoteServAddr, sizeof(remoteServAddr));

		if(rc<0) 
		{
			printf("\nFailed: Could not communicate with the SERVER.");
			close(sd);
			exit(1);
		}

		if(userCommand[0] == 'g' && userCommand[1] == 'e' && userCommand[2] == 't')
		{
			while(1)
			{
				serverSize = sizeof(remoteServAddr);
				rc = recvfrom(sd, message, 100, 0, (struct sockaddr *) &remoteServAddr, &serverSize);
				if(rc<0) 
				{
				  printf("\n << TIME OUT >>\n");
				  printf("%s: Cannot receive data.",argv[0]);
				  continue;
				}

				if(!strcmp(message, "FAILED"))
				{
					printf("\nSERVER says,\nFile does not exist.");
					break;
				}					
				else
				{
				    	if(!strcmp(message,"SUCCESS"))
				    	{
				        	printf("\nSERVER says,\nFile has been copied successfully.");
				            	fclose(file_temp);
				        	break;
				    	}
					if(message[0] == userCommand[4] && message[1] == userCommand[5] && message[2] == userCommand[6])
						file_temp = fopen(message, "w");
					else
				    		fprintf(file_temp,"%s",message);
	
					if(!strcmp(message,"STOP"))
						break;

				}				
			}
		}
		else
		{
			serverSize = sizeof(remoteServAddr);
			printf("\nSERVER says,");
			while(1)
			{
				rc = recvfrom(sd, message, 100, 0, (struct sockaddr *) &remoteServAddr, &serverSize);
				if(!strcmp(message,"STOP"))
					break;
				printf("\n%s", message);
			}
		}
	

	}
  	return 1;
}


















