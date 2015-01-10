/**
* Mark Silvis
* CS1550
* Project 2
* Driver
**/

#include "bankerheader.h"


/* global variables */
int numprocs;		/* number of processes */
int numresources;	/* number of resources */
int *available;		/* available vector */
int **allocation;	/* matrix of allocated resources */
int **max;			/* matrix of max resources */

/* mutex locks */
pthread_mutex_t banker_lock = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t numprocs_lock = PTHREAD_MUTEX_INITIALIZER;


int main(int argc, char *argv[])
{
	/* incorrect usage */
	if (argc < 5 || atoi(argv[2]) <= 1)
	{
		printf("Usage: ./bankers -n <numprocs> -a <available vector>\n");
		printf("<numprocs> must be greater than 1, and there must be at least 1 resource available\n");
		exit(1);
	}

	numprocs = atoi(argv[2]);							/* number of processses */
	available = (int*)malloc((argc-4)*sizeof(int));		/* available vector */
	allocation = (int**)malloc(numprocs*sizeof(int*));	/* allocated matrix */
	max = (int**)malloc(numprocs*sizeof(int*));			/* max matrix */
	pthread_t threads[numprocs];						/* array of threads */
	int thread_nums[numprocs];							/* thread id's */
	int thread;											/* pthread_create return */
	int i, j;											/* indices for loops */
	numresources = argc-4;								/* number of resources */

	srand(time(NULL));									/* seed random number generator */

	/* build available vector */
	numresources = argc-4;
	for (i = 0; i < argc-4; i++)
		available[i] = atoi(argv[4+i]);
	
	/* build allocation matrix */
	for(i = 0; i < numprocs; i++)
		allocation[i] = (int*)malloc((argc-4)*sizeof(int));

	/* set to 0 */
	for(i = 0; i < numprocs; i++)
	{
		for(j = 0; j < argc-4; j++)
		{
			allocation[i][j] = 0;
		}
	}

	/* build max matrix */
	for(i = 0; i < numprocs; i++)
		max[i] = (int*)malloc((argc-4)*sizeof(int));

	/* set to 0 */
	for(i = 0; i < numprocs; i++)
	{
		for(j = 0; j < argc-4; j++)
		{
			max[i][j] = 0;
		}
	}

	/* create threads */
	printf("");
	for(i = 0; i < atoi(argv[2]); i++)
	{
		thread_nums[i] = i;
		if(thread = pthread_create(&threads[i], NULL, thread_start, &thread_nums[i]))
		{
			/* error */
			fprintf(stderr, "Error creating thread\n");
			exit(-1);
		}
	}

	/* continue running as long as more than one thread still exists */
	while (numprocs > 1)
		;

	/* join threads */
	for(i = 0; i < atoi(argv[2]); i++)
		pthread_join(threads[i], NULL);

	/* destroy mutex locks */
	pthread_mutex_destroy(&banker_lock);
	pthread_mutex_destroy(&numprocs_lock);

	/* free malloc'd variables */
	free(available);
	for(i = 0; i < atoi(argv[2]); i++)
	{
		free(allocation[i]);
		free(max[i]);
	}
	free(allocation);
	free(max);

	exit(0);
}
