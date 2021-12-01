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
int relaseSem_perTutti(int semid,int semNum){    //rilascio il semaforo aumentando di 1 il valore del semaforo
    struct sembuf x;
    x.sem_num = semNum;
    x.sem_op = 20;
    x.sem_flg = 0;
    return semop(semid,&x,1);
}
int semAvaiable(int semID,int semNum){ // inizializzo semaforo (da fare solo una volta)
    union semun arg;
    arg.val = 0;
    return semctl(semID,semNum,SETVAL,arg);
}
pid_t nasci(int posizione,  struct type * process){
         pid_t procesPid;
         int tipo=  65 + (rand()%2);
        // printf("Il figlio sara: %d \n", tipo);
         procesPid=fork();

        switch(procesPid){
            case -1:
                printf("Errore creazione figlio! \n");
                exit(0);
                break;
            case 0:
                // printf("[ %d ] Sono appena nato\n", getpid());
                 if(tipo== 65){
                        printf("Sono femmina! PID: %d \n", getpid());
                        char * nuovo = "./a.out"; // PER ADESSO gli dico di  prendere B perchè ancora non esiste A
                        char * num_pro= (char *) malloc (sizeof(int));// serve a passare come argomento il n° identificativo del
                        sprintf(num_pro, "%d", posizione); // scrivo nell stringa il numeo del processo figlio e lo passo come parametro
                        if (execlp(nuovo ,num_pro,(char * ) NULL) <0){ 
                                printf("Bordello con execlp\n");
                            }

                 }else{
                        printf("Sono maschio! PID: %d \n", getpid());
                        char * nuovo = "./b.out"; // PER ADESSO gli dico di  prendere B perchè ancora non esiste A
                        char * num_pro= (char *) malloc (sizeof(int));// serve a passare come argomento il n° identificativo del
                        sprintf(num_pro, "%d", posizione); // scrivo nell stringa il numeo del processo figlio e lo passo come parametro
                        if (execlp(nuovo ,num_pro,(char * ) NULL) <0){ 
                                printf("Bordello con execlp\n");
                            }
                 }
                 
                    break;
            default:
                // LE OPERAZIONI DI SCIRTTURA POSSONO ESSERE FATTE DOPO IL METODO NASCI PER SEMPLIFICARE
                  
                   printf("");
                   int lettera= 65 + rand()%26; //genero lettera del nome
                    process[posizione].nome[0]= (char) lettera;
                    process[posizione].tipo= (char) tipo;
                    lettera= 100 + rand() % 901; // il genoma va da 0 a mille
                    process[posizione].genoma= lettera;
                    process[posizione].cont_rifiuti=0;
                    process[posizione].status=0;
                   
                   
                    return procesPid;
                
                  

        }


    return procesPid ; 


}

int main(){
    /*int femmine=0, maschi=0;
    for (int k=0; k<100 ; k++){
        int random= 65+ rand()%2;
        printf("%d \n", random);
        if(random == 65)
            femmine++;
        if(random == 66)
            maschi++;
    }
    printf("Sono %d femmine e %d maschi \n", femmine, maschi); */
    char* people;
    people = (char*)malloc(sizeof(char)); 

    pid_t procesPid;
    int semID; // SEMAFORO PARTENZA PROCESSI
    int smID; // SEMAFORO MEMORIA CONDIVISA
    int memID = shmget(KEY, sizeof(struct type) *20, IPC_CREAT| 0666); // ID SHARED MEMORY
    if((semID = semget(KEY,1,IPC_CREAT | 0666)) < 0){ // SEMAFORO PARTENZA PROCESSI
        printf("Errno was set to ==> %s\n",strerror(errno));
        exit(-1);
    } 
    if((smID = semget(KEY +1,1,IPC_CREAT | 0666)) < 0){ // SEMAFORO MEMORIA CONDIVISA
        printf("Errno was set to ==> %s\n",strerror(errno));
        exit(-1);
    } 
  
  //___________Creo memoria condivisa con array di 20 struct type
   struct type *b;

	
	if(memID < 0){
		printf("Errore creazione memoria condivisa \n");
		}
	printf("memID nel padre è %d \n", memID);
	
	b = (struct type *)shmat(memID, NULL,  0);
    *people = (char) init_people;
   /* char* argv[] = {"Aldo","Nino","Mattia",NULL};
    char* env[] = {people,NULL}; */

    semAvaiable(semID,0); // inizializzo il semaforo partenza processi
    semAvaiable(smID,0); // inizializzo il semaforo shared  memory
    
    relaseSem(smID,0); // metto il smID a 1
    reserveSem(smID,0);
    for(int i =0; i <init_people;i++){ //CREO 20 processi
        int num_processo=i;
        //prima di scrivere controllo il semaforo ed entro in sezione critica
         
        procesPid =nasci(num_processo,b);

        } 
        relaseSem(smID,0); // ho finito di creare i processi e di scrivere tutti i cazzi, quindi rilascio il semaforo
        
       
       // battezza(num_processo, b);
        
    //}
    switch(procesPid){
        case -1:
            printf("Errore fork\n");
            exit(-1);
        case 0: // FIGLI
            printf("\n ATTENZIONE: in teoria qua non dovevo entrare \n ");
           // printf("[figlio] il mio pid è %d sto per fare la execve\n",getpid());
           /* char *p = "./b.out";
	
            char * num_proc= (char *) malloc (sizeof(int));
	       sprintf(num_proc, "%d", num_processo); // scrivo nell stringa il numeo del processo figlio e lo passo come parametro
	    if (execlp(p ,num_proc,(char * ) NULL) <0){ 
            printf("Bordello con execlp\n");
        }
           */
            exit(0);
        default : // GESTORE
            printf("[padre] il mio pid è %d\n",getpid());
            
            relaseSem_perTutti(semID,0); // QUI partono tutti i processi figli

            
            printf("PID ultimo processo : %d \n", procesPid);
            sleep(5);
            waitpid(procesPid,NULL,0);
             waitpid(procesPid+1,NULL,0);
            printf("Ho aspettato la terminazione dell'ultimo figlio e adesso chiudo tutto\n");
            if((semctl(semID,0,IPC_RMID)) < 0){ // RIMUOVO SEMAFORO PARTENZA
                printf("Errore chiusura semaforo\n");
                exit(-1);
            } 
            if((semctl(smID,0,IPC_RMID)) < 0){ // RIMUOVO SEMAFORO SHARED MEMORY
                printf("Errore chiusura semaforo\n");
                exit(-1);
            } 
            if(shmctl(memID, IPC_RMID,0) <0) { // RIMUOVO SHARED MEMORY
	            printf("rimozione memoria non effettuata");
	        }
            exit(0);
    }
}
