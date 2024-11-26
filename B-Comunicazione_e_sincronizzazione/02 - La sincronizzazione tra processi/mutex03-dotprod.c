//==========================================================================================
//
// Project:  mutex03-dotprod.c
// Date:     01/04/21
// Author:   B. Barney (modificato da Filippo Bilardo)
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//  dot product by pthreads.  
//  Demonstrates thread synchronization with mutexes.
//  gcc -pthread mutex03-dotprod.c
//
// Ver   Date        Comment
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// 1.0   21/10/11    V. iniziale B. Barney, https://computing.llnl.gov/tutorials/pthreads/
// 
//==========================================================================================
#include <stdio.h>    // printf
#include <stdlib.h>   // exit
#include <pthread.h>  // thread	
#include <string.h>

//------------------------------------------------------------------------------------------
void *dotprod(void *arg);

//------------------------------------------------------------------------------------------
/* The following structure contains the necessary information  
to allow the function "dotprod" to access its input data and 
place its output into the structure.  */
typedef struct 
 {
   double      *a;
   double      *b;
   double     sum; 
   int     veclen; 
 } DOTDATA;

/* Define globally accessible variables and a mutex */
#define NUMTHRDS 4
#define VECLEN 100
DOTDATA dotstr; 
pthread_t callThd[NUMTHRDS];
pthread_mutex_t mutexsum;

//------------------------------------------------------------------------------------------
/* The main program creates threads which do all the work and then 
print out result upon completion. Before creating the threads,
the input data is created. Since all threads update a shared structure, we
need a mutex for mutual exclusion. The main thread needs to wait for
all threads to complete, it waits for each one of the threads. We specify
a thread attribute value that allows the main thread to join with the
threads it creates. Note also that we free up handles when they are
no longer needed. */
int main (int argc, char *argv[])
{
  long i;
  double *a, *b;
  void *status;
  pthread_attr_t attr;
  int err;
  
  /* Assign storage and initialize values */
  a = (double*) malloc (NUMTHRDS*VECLEN*sizeof(double));
  b = (double*) malloc (NUMTHRDS*VECLEN*sizeof(double));
  for (i=0; i<VECLEN*NUMTHRDS; i++) {
    a[i]=1;
    b[i]=a[i];
  }
	dotstr.veclen = VECLEN; 
	dotstr.a = a; 
	dotstr.b = b; 
	dotstr.sum = 0;

	err = pthread_mutex_init(&mutexsum, NULL);
	if (err != 0) {
		printf("Error: can't create mutex: %s\n", strerror(err));
		exit(1);
	}
         
	/* Create joinable threads to perform the dotproduct  */
	pthread_attr_init(&attr);
	pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);

	for(i=0;i<NUMTHRDS;i++)
	{
		/* Each thread works on a different set of data.
		 * The offset is specified by 'i'. The size of
		 * the data for each thread is indicated by VECLEN. */
		err = pthread_create(&callThd[i], &attr, dotprod, (void *)i); 
		if (err != 0) {
			printf("Error: can't create thread %ld: %s\n", i, strerror(err));
			exit(1);
		}
	}

	pthread_attr_destroy(&attr);
	
	/* Wait on the other threads */
	for (i=0;i<NUMTHRDS;i++) {
		pthread_join(callThd[i], &status);
	}
	/* After joining, print out the results and cleanup */
	printf("Sum =  %f \n", dotstr.sum);
	free(a);
	free(b);
	pthread_mutex_destroy(&mutexsum);
	pthread_exit(NULL);
}

//------------------------------------------------------------------------------------------
/* The function dotprod is activated when the thread is created.
All input to this routine is obtained from a shared structure 
of type DOTDATA and all output from this function is written into
this shared structure. The benefit of this approach is apparent for the 
multi-threaded program: when a thread is created we pass a single
argument to the activated function - typically this argument
is a thread number. All  the other information required by the 
function is accessed from the globally accessible structure. */
void *dotprod(void *arg) {

  /* Define and use local variables for convenience */
  int i, start, end, len;
  long offset;
  double mysum, *x, *y;
  offset = (long)arg;
   
  len = dotstr.veclen/NUMTHRDS;
  start = offset*len;
  end   = start + len;
  x = dotstr.a;
  y = dotstr.b;

	/* Perform the dot product and assign result
	 * to the appropriate variable in the structure. */
  mysum = 0;
  for (i=start; i<end ; i++) {
    mysum += (x[i] * y[i]);
  }

  /* Lock a mutex prior to updating the value in the shared
   * structure, and unlock it upon updating. */
  pthread_mutex_lock(&mutexsum);
  dotstr.sum += mysum;
  pthread_mutex_unlock(&mutexsum);
  
  pthread_exit((void*) 0);
}
