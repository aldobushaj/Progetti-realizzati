#include "header.h"
int reserveSem(int semid,int semNum){   //riservo il semaforo diminuendo di 1 il valore del semaforo
    struct sembuf x;
    x.sem_num = semNum;
    x.sem_op = -1;
    x.sem_flg = 0;
    return semop(semid,&x,1);
}
int relaseSem(int semid,int semNum){    //rilascio il semaforo aumentando di 1 il valore del semaforo
    struct sembuf x;
    x.sem_num = semNum;
    x.sem_op = 1;
    x.sem_flg = 0;
    return semop(semid,&x,1);
}
int semAvaiable(int semID,int semNum){ // inizializzo semaforo (da fare solo una volta)
    union semun arg;
    arg.val = 0;
    return semctl(semID,semNum,SETVAL,arg);
}


void print(int mio_numero, struct type *b){
	printf("[ %d ] Status: %d \n",getpid(), b[mio_numero].status );
	printf("[ %d ] Genoma: %lu \n",getpid(), b[mio_numero].genoma );
	printf("[ %d ] Tipo: %c \n",getpid(), b[mio_numero].tipo );
	printf("[ %d ] Tipo: %c \n",getpid(), b[mio_numero].cont_rifiuti );
	printf("[ %d ] Nome: ",getpid());
	for(int i=0; i<20;i++){ // Ciclo per stampare tutto l'array contenente il nome
		printf("%c", b[mio_numero].nome[i]);

	}
	printf("\n");
	
}

int main(int argc, char *argv[]) {
	int semID = semget(KEY,1,0); // SEMAFORO PARTENZA PROCESSI
	if (semID <0){
		printf("Errore semaforo partenza processi nel figlio! \n");
	}
	reserveSem(semID,0); // attendo che il gestore mi faccia partire
  
   int memID= shmget(KEY, sizeof(struct type), 0); // ID SHARED MEMORY
   int smID = semget(KEY +1,1,0); // SEMAFORO SHARED MEMORY
    // eseguo i vari controlli sugli ID letti dal figlio
	if (memID <0){
		printf("Errore memoria figlio! \n");
	}
	
	
	if(smID <0){
		printf("Errore semaforo shared memory figlio \n");
	}
	int mio_numero = atoi(argv[0]); // Converto il numero del processo da string a int
	struct type * b=  (struct type *) shmat(memID, NULL,  0); // MI COLLEGO ALLA SHARED MEMORY


    // ***************** da qui posso far fare cose al figlio sulla shared memory
	//ACCEDO ALLA MEMORIA PER LEGGERE E RISERVO IL SEMAFORO
	/* reserveSem(smID,0);
	print(mio_numero, b);
	relaseSem(smID, 0); */
	// esco sezione critica
	exit(0);
} 



