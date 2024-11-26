//==========================================================================================
//
// Project:  mutex04-randsafe.c
// Date:     01/04/21
// Author:   K.A. Robbins and S. Robbins (modificato da Filippo Bilardo)
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//  Demonstrates use of mutex to protect an unsafe library function. 
//  Tratto da K.A. Robbins and S. Robbins, UNIX systems programming: communication, 
//  concurrency, and threads
//  gcc -pthread mutex04-randsafe.c
//
// Ver   Date        Comment
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// 1.0   24/10/11    V. iniziale K.A. Robbins and S. Robbins, UNIX systems programming
// 
//==========================================================================================
#include <stdio.h>    // printf
#include <stdlib.h>   // exit
#include <pthread.h>  // thread	
#include <string.h>

//------------------------------------------------------------------------------------------
int randsafe(double *ranp);
void *PrintRand(void *threadid);

//------------------------------------------------------------------------------------------
#define NUM_THREADS	10

//------------------------------------------------------------------------------------------
int main(int argc, char *argv[])
{
	pthread_t threads[NUM_THREADS];
	int err;
	long t;
		
	for (t=0;t<NUM_THREADS;t++) {
		printf("In main: creating thread %ld\n", t);
		err = pthread_create(&threads[t], NULL, PrintRand, (void *)t);
		if (err != 0) {
			printf("Error: can't create thread %ld: %s\n", t, strerror(err));
			exit(1);
		}
	}
	pthread_exit(NULL);
}

//------------------------------------------------------------------------------------------
int randsafe(double *ranp) 
{ 
	static pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER; 
	int error; 

	if (error = pthread_mutex_lock(&lock)) 
		return error; 
  // in alternativa, se la funzione deve restituire un vaolore tra 0 e RAD_MAX
  // basta sostituire con *ranp = rand();
	*ranp = (rand() + 0.5)/(RAND_MAX + 1.0); 
	return pthread_mutex_unlock(&lock); 
} 

void *PrintRand(void *threadid)
{
	long tid;
	int err;
	double num;
   
	tid = (long)threadid;
	err = randsafe(&num);
	if (err != 0) {
		printf("Error on mutex for thread %ld: %s\n", tid, strerror(err));
		exit(1);
	}
	printf("Thread #%ld: random=%lf\n", tid, num);
	pthread_exit(NULL);
}
