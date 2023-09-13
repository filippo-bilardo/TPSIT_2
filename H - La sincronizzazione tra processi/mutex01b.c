//========================================================================================== 
// 
// Project: mutex01b.c 
// Date: 27/12/21 
// Author: https://www.bogotobogo.com/cplusplus/multithreading_pthread.php 
// 
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
// 
// Demonstrates thread synchronization with mutexes. 
// gcc -pthread mutex01b.c 
// 
// Ver  Date      Comment 
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
// 1.0  27/12/21  Versione iniziale 
// 
//==========================================================================================
#include <stdio.h> 
#include <pthread.h>
#include <unistd.h>  //usleep

volatile int counter=0;
//pthread_mutex_t myMutex;

void *mutex_testing(void *param) {
  int i;
  for(i=0; i<5; i++) {
    //pthread_mutex_lock(&myMutex);
    counter++;
    usleep(1);
    printf("thread %d counter = %d\n", (int)param, counter);
    //pthread_mutex_unlock(&myMutex);
  }
}

int main(int argc, char *argv[]) {
  int one=1, two=2, three=3;
  pthread_t thread1, thread2, thread3;
  //pthread_mutex_init(&myMutex, 0);
  pthread_create(&thread1, 0, mutex_testing, (void*)one);
  pthread_create(&thread2, 0, mutex_testing, (void*)two);
  pthread_create(&thread3, 0, mutex_testing, (void*)three);
  pthread_join(thread1, 0);
  pthread_join(thread2, 0);
  pthread_join(thread3, 0);
  //pthread_mutex_destroy(&myMutex);
  return 0;
}
