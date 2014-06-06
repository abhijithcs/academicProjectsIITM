#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define LOCAL_SERVER_PORT 8000
#define REMOTE_CLIENT_PORT 8500
#define MAX_MSG 100

int main(int argc, char *argv[]) {
  
  	int sd, rc, n, cliLen, i;
  	struct sockaddr_in cliAddr, servAddr;
  	char command[MAX_MSG];

//Creating the Socket.
  	sd=socket(AF_INET, SOCK_DGRAM, 0);
  	if(sd<0) 
	{
    		printf("%s: Cannot open socket \n",argv[0]);
    		exit(1);
  	}

//Bind local server PORT
  	servAddr.sin_family = AF_INET;
  	servAddr.sin_addr.s_addr = htonl(INADDR_ANY);
  	servAddr.sin_port = htons(LOCAL_SERVER_PORT);
  	rc = bind (sd, (struct sockaddr *) &servAddr,sizeof(servAddr));
  	
	if(rc<0) 
	{
    		printf("%s: Cannot bind port number %d \n", 
	   	argv[0], LOCAL_SERVER_PORT);
    		exit(1);
  	}
  	
	printf("%s: Waiting for a COMMAND (on port UDP %u) from the Client\n", 
   	argv[0],LOCAL_SERVER_PORT);



//*******************************
//SERVER END TO RECEIVE COMMANDS
//*******************************
	
	int count = 0;
	char lsList[100];
	FILE *file_temp;

	while(1)
	{
		n = recvfrom(sd, command, MAX_MSG, 0, (struct sockaddr *) &cliAddr, &cliLen);

		printf("\nCLIENT says, %s", command);

		if(!strcmp(command,"open"))
		{
			n = sendto(sd, "Server is now ready to respond to your COMMANDs.", 100, 0, (struct sockaddr *) &cliAddr, cliLen);
			int s = chdir ("/");
			count++;
			n = sendto(sd, "STOP", 100, 0, (struct sockaddr *) &cliAddr, cliLen);
			continue;
		}
		else if(!strcmp(command,"ls")&& count != 0)
		{
			FILE *file1 = popen(command, "r");
			while (fgets(lsList, 100, file1)) 
			{
				n = sendto(sd, lsList, 100, 0, (struct sockaddr *) &cliAddr, cliLen);
			}
			n = sendto(sd, "STOP", 100, 0, (struct sockaddr *) &cliAddr, cliLen);	
			count++;		

		}
		else if(command[0]=='c' && command[1]=='d' && count != 0)
		{
			i = 0;
			char destination[50];
			while(command[i+3] != '\n')
			{
				destination[i] = command[i+3];
				i++;
			}
			
			if(!chdir(destination))	
				n = sendto(sd, "Directory has been successfully changed.", 100, 0, (struct sockaddr *) &cliAddr, cliLen);
			else
				n = sendto(sd, "Such a Directory doesn't exist.", 100, 0, (struct sockaddr *) &cliAddr, cliLen);				
			n = sendto(sd, "STOP", 100, 0, (struct sockaddr *) &cliAddr, cliLen);	
			count++;	
		}
		else if(!strcmp(command,"pwd") && count != 0)
		{
			FILE *file2 = popen(command, "r");
			while (fgets(lsList, 100, file2)) 
			{
				n = sendto(sd, lsList, 100, 0, (struct sockaddr *) &cliAddr, cliLen);
			}
			n = sendto(sd, "STOP", 100, 0, (struct sockaddr *) &cliAddr, cliLen);	
			count++;		

		}
		else if(command[0]=='g' && command[1]=='e' && command[2]=='t' && count != 0)
		{
			i = 0;
			char output[100], fileName[50];
			while(command[i+4] != '\n')
			{
				fileName[i] = command[i+4];
				i++;
			}

                    	file_temp = fopen(fileName,"r");
			printf("\nChecking for %s", fileName);

                    	if(file_temp == NULL)
                    	{	printf("\n%s is not Found.", fileName);
				n = sendto(sd, "FAILED", 100, 0, (struct sockaddr *) &cliAddr, cliLen);
                    	}
                    	else
                    	{
				printf("\n%s is Found.", fileName);
				n = sendto(sd, fileName, 100, 0, (struct sockaddr *) &cliAddr, cliLen);
				while(fgets(output,100,file_temp))
				{
                            		n = sendto(sd, output, 100, 0, (struct sockaddr *) &cliAddr, cliLen);
				}
				n = sendto(sd, "SUCCESS", 100, 0, (struct sockaddr *) &cliAddr, cliLen);	
                    	}
			n = sendto(sd, "STOP", 100, 0, (struct sockaddr *) &cliAddr, cliLen);	
			count++;
		}
		else if(!strcmp(command,"done"))
		{
			n = sendto(sd, "Server is now CLOSED.", 100, 0, (struct sockaddr *) &cliAddr, cliLen);
			n = sendto(sd, "STOP", 100, 0, (struct sockaddr *) &cliAddr, cliLen);
			count = 0;
		}
		else
		{
			if(count == 0)
			{
				n = sendto(sd, "You have to enter 'open' first to request a response from the SERVER.", 100, 0, (struct sockaddr *) &cliAddr, cliLen);
				n = sendto(sd, "STOP", 100, 0, (struct sockaddr *) &cliAddr, cliLen);
				continue;
			}
			n = sendto(sd, "It is not a valid Command.", 100, 0, (struct sockaddr *) &cliAddr, cliLen);
			n = sendto(sd, "STOP", 100, 0, (struct sockaddr *) &cliAddr, cliLen);
		}

	


	}
return 0;
}
