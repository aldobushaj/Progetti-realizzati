#include "header.h"



// CREO UN SEGMENTO DI MEMORIA CONDIVISA E SCRIVO UN INTERO. IL FIGLIO LO METTE A 1 

int main(int argc, char *argv[]) {
	
  
   int memID= shmget(KEY, sizeof(struct type), 0);
	if (memID <0){
		printf("Errore memoria figlio! \n");
	}
	printf("memID nel figlio è %d \n", memID);
	
	struct type * b=  (struct type *) shmat(memID, NULL,  0);
	printf("Valore salvato in sm nel figlio  prima di scrittura %d \n", b[0].status );
	 b[0].status= 1001;
	printf("Valore salvato in sm nel figlio dopo  scrittura %d \n", b[0].status );
	printf("Valore salvato in sm nel figlio dopo  scrittura %d \n", b[19].status );
	 b[19].status= 99999;
	printf("Valore salvato in sm nel figlio dopo  scrittura %d \n", b[19].status );
	exit(EXIT_SUCCESS);
} 



