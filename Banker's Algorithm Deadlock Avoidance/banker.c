/**
* Mark Silvis
* CS1550
* Project 2
* Banker's algorithm
**/

#include "bankerheader.h"


/* request reources */
int request_resources(int pid, int resources[])
{
	int i=0;		/* index for loops */

	/* print request */
	printf("Process %d requests\t<", pid);
	for(i=0; i<numresources-1; i++)
		printf("%d, ", resources[i]);
	printf("%d>\n", resources[i]);
	for(i=0; i<numresources; i++)
	{
		allocation[pid][i] += resources[i];
		available[i] -= resources[i];
	}

	/* if safe state */
	if(safety_algorithm() == 0)
	{
		printf("State is safe\n");
		printf("Resources available\t<");
		for(i=0; i<numresources-1; i++)
			printf("%d, ", available[i]);
		printf("%d>\n\n", available[i]);
	}

	/* if unsafe state */
	else
	{
		printf("State is unsafe\n\n");
		for(i=0; i<numresources; i++)
		{
			available[i] += allocation[pid][i];
			allocation[pid][i] = 0;
		}		
		return -1;
	}
	return 0;
}

/* release resources */
void release_resources(int pid, int resources[])
{
	int i=0;		/* index for loops */

	/* print release */
	printf("Process %d releases\t<", pid);
	for(i=0; i<numresources-1; i++)
		printf("%d, ", resources[i]);
	printf("%d>\n", resources[i]);

	for(i=0; i<numresources; i++)
	{
		allocation[pid][i] -= resources[i];
		available[i] += resources[i];
	}
	printf("Resources available\t<");
	for(i=0; i<numresources-1; i++)
		printf("%d, ", available[i]);
	printf("%d>\n\n", available[i]);
}

/* check for unsafe state */
int safety_algorithm()
{
	/* variables representing resources for theorectical completion */
	/* follows safety algorithm */
	int work[numresources];	/* theoretical available vector */
	int finish[numprocs];	/* completion vector */
	int safe = 0;			/* true when all items in finish[] are 0 */
	int found;				/* true when finish[i] == false and needi <= work */
	int i, j;				/* indices for loops */

	/* build work array */
	for(i = 0; i < numresources; i++)
		work[i] = available[i];

	/* build finish array */
	for(i = 0; i < numprocs; i++)
		finish[i] = 1;

	/* check for unsafe state */
	for(i = 0; i < numprocs; i++)
	{
		/* find index i such that finish[i] == false */
		if(finish[i])
		{
			found = 1;
			for(j = 0; j < numresources; j++)
			{
				/* and max[i]-allocated[i] <= work */
				if((max[i][j]-allocation[i][j]) > work[j])
				{
					found = 0;
					break;
				}
			}
			if(found)
			{
				for (j = 0; j < numresources; j++)
					work[j] = work[j] + allocation[i][j];
				safe = 1;
				finish[i] = 0;
				break;
			}
		}
	}
	/* not safe */
	if(!safe)
		return -1;
	/* safe */
	else
		return 0;
}