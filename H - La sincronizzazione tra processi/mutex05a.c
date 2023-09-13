//==========================================================================================
//
// Project:  mutex05a.c
// Date:     27/12/21
// Author:   https://www.bogotobogo.com/cplusplus/multithreaded.php
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//  Demonstrates thread synchronization with mutexes.
//  gcc -pthread mutex05a.c
//
// Ver   Date        Comment
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// 1.0   27/12/21    Versione iniziale
// 
//==========================================================================================
#include <stdio.h>
#include <pthread.h>

static volatile int balance=0;
pthread_mutex_t myMutex;

void *deposit(void *param)
{
  char *who=param;
  int i;

  printf("%s: begin\n", who);
  for (i=0; i<1000000; i++) {
    // critical section
    pthread_mutex_lock(&myMutex);
    balance = balance+1;
    pthread_mutex_unlock(&myMutex);
  }
  printf("%s: done\n", who);
  return NULL;
}

int main()
{
  pthread_t p1, p2;
  printf("main() starts depositing, balance = %d\n", balance);

  pthread_mutex_init(&myMutex,0);
  pthread_create(&p1, NULL, deposit, "A");
  pthread_create(&p2, NULL, deposit, "B");
  
  // join waits for the threads to finish
  pthread_join(p1, NULL);
  pthread_join(p2, NULL);
  pthread_mutex_destroy(&myMutex);
  printf("main() A and B finished, balance = %d\n", balance);
  return 0;
}
