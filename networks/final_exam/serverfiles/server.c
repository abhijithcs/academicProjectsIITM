#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h> /* memset() */
#include <stdlib.h>
#include <openssl/md5.h>

#include <netinet/in.h>
#include <stdio.h>
#include <unistd.h> /* close */

#include <openssl/md5.h>

#define LOCAL_SERVER_PORT 1500
#define MAX_MSG 100
#define SUCCESS 0
#define ERROR   1

#define END_LINE 0x0

int main(int argc, char *argv[]) {
  
  int sd, rc, n, cliLen;
  struct sockaddr_in cliAddr, servAddr;
  char msg[MAX_MSG];
  char reply[MAX_MSG];

  /* socket creation */
  sd=socket(AF_INET, SOCK_DGRAM, 0);
  if(sd<0) {
    printf("%s: cannot open socket \n",argv[0]);
    exit(1);
  }

  /* bind local server port */
  servAddr.sin_family = AF_INET;
  servAddr.sin_addr.s_addr = htonl(INADDR_ANY);
  servAddr.sin_port = htons(LOCAL_SERVER_PORT);
  rc = bind (sd, (struct sockaddr *) &servAddr,sizeof(servAddr));
  if(rc<0) {
    printf("%s: cannot bind port number %d \n", 
	   argv[0], LOCAL_SERVER_PORT);
    exit(1);
  }

  printf("%s: waiting for data on port UDP %u\n", 
	   argv[0],LOCAL_SERVER_PORT);

  /* server infinite loop */
  while(1) {
  
    /* init buffer */
    memset(msg,0x0,MAX_MSG);
   


    /* receive message */
    cliLen = sizeof(cliAddr);
    n = recvfrom(sd, msg, MAX_MSG, 0, 
		 (struct sockaddr *) &cliAddr, &cliLen);

    if(n<0) {
      printf("%s: cannot receive data \n",argv[0]);
      continue;
    }
      
    char *naddr;
    unsigned int nport;
   
    naddr = inet_ntoa(cliAddr.sin_addr);
    nport = ntohs(cliAddr.sin_port);
 
    
    /* print received message */
    printf("%s: from %s: UDP%u : %s \n", 
	   argv[0],naddr,
	  nport,msg);
   
    int randval = rand()%1400;

    int portnumber = 7500+randval;

    sprintf(reply, "%d",portnumber);

    rc = sendto(sd,  reply, strlen(reply)+1,0, 
		(struct sockaddr *) &cliAddr, 
		sizeof(cliAddr));
   //printf("Assigning port number %d for %s with roll number %s\n",portnumber,naddr,msg);
//#if 0
   {
   /*for TCP connection*/
       int tcpsd, newSd;
       char line[MAX_MSG];

       tcpsd = socket(AF_INET, SOCK_STREAM, 0);
       if(tcpsd<0) {
         perror("cannot open socket ");
         return 1;
       }
  printf("tcp socket opened\n");  
fflush(stdout);
       /* bind server port */
       servAddr.sin_family = AF_INET;
       servAddr.sin_addr.s_addr = htonl(INADDR_ANY);
       servAddr.sin_port = htons(portnumber);
  
       if(bind(tcpsd, (struct sockaddr *) &servAddr, sizeof(servAddr))<0) {
          perror("cannot bind port ");
          return 1;
       }

       listen(tcpsd,1);
	
       //printf("%s: waiting for data on port TCP %u\n",argv[0],portnumber);
       fflush(stdout);

       cliLen = sizeof(cliAddr);
       newSd = accept(tcpsd, (struct sockaddr *) &cliAddr, &cliLen);
       if(newSd<0) {
          perror("cannot accept connection ");
          return 1	;
       }
    
       /* init line */
       memset(line,0x0,MAX_MSG);
    
      /* receive segments */
      while(read_line(newSd,line)!=1) {
            //printf("%s: received from %s:TCP%d : %s\n", argv[0], 
	     //inet_ntoa(cliAddr.sin_addr),
	     //ntohs(cliAddr.sin_port), line);
          /* init line */

         int i;
         unsigned char result[MD5_DIGEST_LENGTH];
         unsigned char md5return[200];
 
	strcat(line," ");
        strcat(line,reply);
	strcat(line," ");
        strcat(line,inet_ntoa(cliAddr.sin_addr));
	//printf("line: %s\n",line);
         MD5(line, strlen(line), result);
        for(i = 0; i < MD5_DIGEST_LENGTH; i++){
           sprintf(&md5return[2*i],"%02x", result[i]);
            //printf("%02x",result[i]);
        }
         md5return[2*i] = '\0';
          memset(line,0x0,MAX_MSG);
          //printf("MD5 to be returned: %s\n",md5return);
//          sleep(10);
          rc = send(newSd, md5return, strlen(md5return) + 1, 0);
          if(rc < 0){
            perror("Unable to send data");
          }
      }
   }


//#endif    
  }/* end of server infinite loop */

return 0;

}



/* WARNING WARNING WARNING WARNING WARNING WARNING WARNING       */
/* this function is experimental.. I don't know yet if it works  */
/* correctly or not. Use Steven's readline() function to have    */
/* something robust.                                             */
/* WARNING WARNING WARNING WARNING WARNING WARNING WARNING       */

/* rcv_line is my function readline(). Data is read from the socket when */
/* needed, but not byte after bytes. All the received data is read.      */
/* This means only one call to recv(), instead of one call for           */
/* each received byte.                                                   */
/* You can set END_CHAR to whatever means endofline for you. (0x0A is \n)*/
/* read_lin returns the number of bytes returned in line_to_return       */
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
