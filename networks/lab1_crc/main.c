#include<stdio.h>
#include <stdlib.h>
#include <time.h>

#define INPUT_LENGTH 128
#define GENERATOR_LENGTH 33 
#define CRC_LENGTH 160


void main()
{
	char c;
	int i=0, j, m, n, g;
	int inputString[CRC_LENGTH];
	char polynomial[GENERATOR_LENGTH]="100000100110000010010110111111011";
	int processString[GENERATOR_LENGTH];
	int processedString[CRC_LENGTH];

//Encoding - Sender End:
/*	FILE * file;
	file = fopen("infile", "r");

if(file) 
{
*/
for(g=0; g<50; g++)
{
	i=0;
/*
	while ((c = getc(file)) != EOF)
	{
		if(c != '\n')
		{
			inputString[i] = c - '0';			
			i++;
			if(i == INPUT_LENGTH)
				break;
		}
	}
*/
	while (i<INPUT_LENGTH)
	{
		scanf("%c", &c);
		if(c != '\n')
		{
			inputString[i] = c - '0';			
			i++;
			if(i == INPUT_LENGTH)
				break;
		}
	}


	for(j=0; j<GENERATOR_LENGTH; j++)
	{	
		inputString[INPUT_LENGTH+j] = 0;
		processString[j] = polynomial[j] - '0';
	}

	for(j=0; j<CRC_LENGTH; j++)
		processedString[j] = inputString[j];
	
	for(m=0; m<INPUT_LENGTH; m++)
	{
		if(processedString[m]==1)
		{
			for(n=0; n<GENERATOR_LENGTH; n++)
				processedString[m+n] = getXOR(processedString[m+n], processString[n]);
		}
	}

	for(j=0; j<GENERATOR_LENGTH-1; j++)
	{
		inputString[INPUT_LENGTH+j] = processedString[INPUT_LENGTH+j];
	}

	printf("\nInput: ");
	for(j=0; j<=CRC_LENGTH-GENERATOR_LENGTH; j++)
		printf("%d",inputString[j]);
	printf("\nCRC: ");
	for(j=0; j<CRC_LENGTH; j++)
		printf("%d",inputString[j]);

// A) Error Detection - Receiver End:

	int errorStringSize = CRC_LENGTH + GENERATOR_LENGTH - 1;
	int errorString[errorStringSize];
	int errorCheck[errorStringSize];
	int errorBit;
	int z;
	int flag=0;

	srand(time(NULL));
	for(z=0; z<10; z++)
	{	
		for(j=0; j<CRC_LENGTH; j++) 
			errorString[j] = inputString[j];

		errorBit = rand()%CRC_LENGTH;

		if(errorString[errorBit] == 1)
			errorString[errorBit] = 0;
		else
			errorString[errorBit] = 1;


		for(m=0; m<GENERATOR_LENGTH-1; m++)
		{
			errorString[CRC_LENGTH + m] = 0;
		}
		for(m=0; m<errorStringSize; m++)
		{
			errorCheck[m] = errorString[m];
		}

		for(m=0; m<CRC_LENGTH; m++)
		{
			if(errorCheck[m] == 1)
			{
				for(n=0; n<GENERATOR_LENGTH; n++)
					errorCheck[m+n] = getXOR(errorCheck[m+n], processString[n]);
			}
		}

		printf("\nOriginal String with CRC: ");
		for(j=0; j<CRC_LENGTH; j++)
			printf("%d",inputString[j]);

		printf("\nCorrupted String: ");
		for(j=0; j<CRC_LENGTH; j++)
			printf("%d",errorString[j]);

		flag = 0;
		for(j=CRC_LENGTH; j<errorStringSize; j++)
			flag = flag + errorCheck[j];
		if(flag == 0)
			printf("\nCRC Check: Passed");
		else
			printf("\nCRC Check: Failed");
	}

// B) Error Detection - Receiver End:

	for(z=0; z<10; z++)
	{	
		for(j=0; j<CRC_LENGTH; j++) 
			errorString[j] = inputString[j];

		errorBit = rand()%CRC_LENGTH;

		if(errorString[errorBit] == 1)
		{
			errorString[errorBit] = 0;

			if(errorString[errorBit+1] == 1)
				errorString[errorBit+1] = 0;
			else
				errorString[errorBit+1] = 1;
		}
		else
		{
			errorString[errorBit] = 1;

			if(errorString[errorBit+1] == 1)
				errorString[errorBit+1] = 0;
			else
				errorString[errorBit+1] = 1;
		}


		for(m=0; m<GENERATOR_LENGTH-1; m++)
		{
			errorString[CRC_LENGTH + m] = 0;
		}
		for(m=0; m<errorStringSize; m++)
		{
			errorCheck[m] = errorString[m];
		}

		for(m=0; m<CRC_LENGTH; m++)
		{
			if(errorCheck[m] == 1)
			{
				for(n=0; n<GENERATOR_LENGTH; n++)
					errorCheck[m+n] = getXOR(errorCheck[m+n], processString[n]);
			}
		}

		printf("\nOriginal String with CRC: ");
		for(j=0; j<CRC_LENGTH; j++)
			printf("%d",inputString[j]);

		printf("\nCorrupted String: ");
		for(j=0; j<CRC_LENGTH; j++)
			printf("%d",errorString[j]);

		flag = 0;
		for(j=CRC_LENGTH; j<errorStringSize; j++)
			flag = flag + errorCheck[j];
		if(flag == 0)
			printf("\nCRC Check: Passed");
		else
			printf("\nCRC Check: Failed");
	}

// C) Error Detection - Receiver End:

	int oddNum, p;
	
	for(z=0; z<10; z++)
	{	
		for(j=0; j<CRC_LENGTH; j++) 
			errorString[j] = inputString[j];

		errorBit = rand()%CRC_LENGTH;
		oddNum = (rand()%12)+ 3;
		if(oddNum%2 == 0)
			oddNum = oddNum-1;
		p=1;
		if(errorString[errorBit] == 1)
		{
			errorString[errorBit] = 0;
			while(p < oddNum)
			{
				if(errorString[errorBit+p] == 1)
					errorString[errorBit+p] = 0;
				else
					errorString[errorBit+p] = 1;
				p++;
			}
		}
		else
		{
			errorString[errorBit] = 1;
			while(p < oddNum)
			{
				if(errorString[errorBit+p] == 1)
					errorString[errorBit+p] = 0;
				else
					errorString[errorBit+p] = 1;
				p++;
			}
		}


		for(m=0; m<GENERATOR_LENGTH-1; m++)
		{
			errorString[CRC_LENGTH + m] = 0;
		}
		for(m=0; m<errorStringSize; m++)
		{
			errorCheck[m] = errorString[m];
		}

		for(m=0; m<CRC_LENGTH; m++)
		{
			if(errorCheck[m] == 1)
			{
				for(n=0; n<GENERATOR_LENGTH; n++)
					errorCheck[m+n] = getXOR(errorCheck[m+n], processString[n]);
			}
		}

		printf("\nOriginal String with CRC: ");
		for(j=0; j<CRC_LENGTH; j++)
			printf("%d",inputString[j]);

		printf("\nCorrupted String: ");
		for(j=0; j<CRC_LENGTH; j++)
			printf("%d",errorString[j]);

		printf("\nNumber of Errors: %d",oddNum);

		flag = 0;
		for(j=CRC_LENGTH; j<errorStringSize; j++)
			flag = flag + errorCheck[j];
		if(flag == 0)
			printf("\nCRC Check: Passed");
		else
			printf("\nCRC Check: Failed");
	}


// D) Error Detection - Receiver End:

	for(z=0; z<10; z++)
	{	
		for(j=0; j<CRC_LENGTH; j++) 
			errorString[j] = inputString[j];

		errorBit = rand()%CRC_LENGTH;

		p=1;
		if(errorString[errorBit] == 1)
		{
			errorString[errorBit] = 0;
			while(p < 30)
			{
				if(errorString[errorBit+p] == 1)
					errorString[errorBit+p] = 0;
				else
					errorString[errorBit+p] = 1;
				p++;
			}
		}
		else
		{
			errorString[errorBit] = 1;
			while(p < 30)
			{
				if(errorString[errorBit+p] == 1)
					errorString[errorBit+p] = 0;
				else
					errorString[errorBit+p] = 1;
				p++;
			}
		}


		for(m=0; m<GENERATOR_LENGTH-1; m++)
		{
			errorString[CRC_LENGTH + m] = 0;
		}
		for(m=0; m<errorStringSize; m++)
		{
			errorCheck[m] = errorString[m];
		}

		for(m=0; m<CRC_LENGTH; m++)
		{
			if(errorCheck[m] == 1)
			{
				for(n=0; n<GENERATOR_LENGTH; n++)
					errorCheck[m+n] = getXOR(errorCheck[m+n], processString[n]);
			}
		}

		printf("\nOriginal String with CRC: ");
		for(j=0; j<CRC_LENGTH; j++)
			printf("%d",inputString[j]);

		printf("\nCorrupted String: ");
		for(j=0; j<CRC_LENGTH; j++)
			printf("%d",errorString[j]);

		flag = 0;
		for(j=CRC_LENGTH; j<errorStringSize; j++)
			flag = flag + errorCheck[j];
		if(flag == 0)
			printf("\nCRC Check: Passed");
		else
			printf("\nCRC Check: Failed");
	}
}

//}
//fclose(file);
}

int getXOR(int num1, int num2)
{
	if(num1 == 1 && num2 == 0)
		return 1;
	else if(num2 == 1 && num1 == 0)
		return 1;
	else
		return 0;
}
