/**
* Mark Silvis
* CS1550
* Project 2
* Thread routine
**/

#include "bankerheader.h"


/* mutex locks */
extern pthread_mutex_t banker_lock;
extern pthread_mutex_t numprocs_lock;


/* start routine for threads */
void *thread_start(void *arg)
{
	const int tid = *((int *)arg);	/* thread id */
	int request[numresources];		/* array for requests */
	int i;							/* index for loops */

	/* build thread's max resources */
	for(i = 0; i < numresources; i++)
	{
		if(available[i] > 0)
			max[tid][i] = rand() % available[i];
		else
			max[tid][i] = 0;
	}

	while(1)
	{
		/* return if only one thread remains */
		if(numprocs==1)
			return NULL;

		/* request */
		/* generate random array for request */
		for(i = 0; i < numresources; i++)
		{
			if((max[tid][i]-allocation[tid][i]) > 0)
				request[i] = rand() % (max[tid][i]-allocation[tid][i]);
			else
				request[i] = 0;
		}

		/* lock and make request */
		pthread_mutex_lock(&banker_lock);
		if(request_resources(tid, request))
		{
			/* request results in unsafe state */
			pthread_mutex_unlock(&banker_lock);
			pthread_mutex_lock(&numprocs_lock);
			numprocs--;
			pthread_mutex_unlock(&numprocs_lock);
			return NULL;
		}
		pthread_mutex_unlock(&banker_lock);
	
		/* sleep */
		sleep(rand() % 3 + 1);
	
		/* release */
		/* generate random array for release */
		for(i = 0; i < numresources; i++)
		{
			if(allocation[tid][i] > 0)
				request[i] = rand() % (allocation[tid][i]);
			else
				request[i] = 0;
		}
		/* lock and make release */
		pthread_mutex_lock(&banker_lock);
		release_resources(tid, request);
		pthread_mutex_unlock(&banker_lock);
		
		/* sleep */
		sleep(rand() % 3 + 1);
	}

	return NULL;
}