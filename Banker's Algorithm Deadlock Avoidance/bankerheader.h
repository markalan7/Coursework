/**
* Mark Silvis
* CS1550
* Project 2
* Header file
**/

/* headers */
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <time.h>			/* for random number generator */
#include <pthread.h>		/* include threading */

/* prototypes */
extern int numprocs;		/* number of processes */
extern int numresources;	/* number of resources */
extern int *available;		/* available vector */
extern int **allocation;	/* matrix of allocated resources */
extern int **max;			/* matrix of max resources */

/* banker.c */
int request_resources(int pid, int resources[]);
void release_resources(int pid, int resources[]);

/* process.c */
void *thread_start(void *arg);

/* driver.c */