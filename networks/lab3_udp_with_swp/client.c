#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define REMOTE_SERVER_PORT 8000
#define LOCAL_CLIENT_PORT 8500
#define WINDOW_SIZE 4

int main(int argc, char *argv[]) {
  
 	int sd, rc, i, n, j, p, k;
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
	int value;
	int startCopying = 0;
	unsigned char byte;
	int successLog[WINDOW_SIZE];
	int ackMessage = 0;
	char ackReply[1];
	int packetIndex, sequenceNumber;
	int lastPacket = 0;
	int t;

	int index = 0;
	char packet[99];
	char ackCount[1];
	int ackCountIn;
	char packetNumberCh[1];
	int packetNumberIn;

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
			startCopying = 0;
			lastPacket = 0;
			serverSize = sizeof(remoteServAddr);
			rc = recvfrom(sd, message, 100, 0, (struct sockaddr *) &remoteServAddr, &serverSize);

printf("\nCLIENT: First reply from the SERVER. %s is found.", message);
			
			if(rc<0) 
			{
			  	printf("\n << TIME OUT >>\n");
			  	printf("%s: Cannot receive data.",argv[0]);
			  	continue;
			}

			if(!strcmp(message, "FAILED"))
			{
				printf("\nSERVER says,\nFile does not exist.");
				startCopying = 0;
			}					
			else
			{
				file_temp = fopen(message, "w");
				startCopying = 1;

printf("\nCLIENT: File %s is opened.", message);

			}
		

			while(startCopying == 1)
			{
				sequenceNumber = 0;
				ackMessage = 0;

				for(p=0; p<WINDOW_SIZE; p++)
				{
					serverSize = sizeof(remoteServAddr);
					rc = recvfrom(sd, message, 100, 0, (struct sockaddr *) &remoteServAddr, &serverSize);

					if(rc<0) 
					{
					  printf("%s: Cannot receive data.",argv[0]);
					  continue;
					}
    					
					if(!strcmp(message,"END"))
				    	{
						printf("\nSERVER says,\nLast packet has been sent to CLIENT successfully.");
						lastPacket = 1;
					    	fclose(file_temp);
						startCopying = 0;
						break;
				    	}
					else
					{
						packetNumberCh[0] = message[99];
						packetNumberIn = atoi(packetNumberCh);
						if(packetNumberIn == sequenceNumber)
						{

printf("\nCLIENT: Packet[%d] is successfully arrived.",packetNumberIn);

							for(j=0; j<99; j++)
								packet[j] = message[j];

							fwrite(packet, sizeof(char), sizeof(packet), file_temp);
							sequenceNumber++;
							sprintf(ackCount,"%d",sequenceNumber);
						}
						else
						{
							sprintf(ackCount,"%d",sequenceNumber);
							break;
						}

						if(sequenceNumber == WINDOW_SIZE)
							break;
					}	
				}
				
				if(lastPacket == 0)
				{

printf("\nCLIENT: Request to resend last %d packets from the SERVER.", atoi(ackCount)-4);
					rc = sendto(sd, ackCount, 1, 0, (struct sockaddr *) &remoteServAddr, sizeof(remoteServAddr));		
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


















