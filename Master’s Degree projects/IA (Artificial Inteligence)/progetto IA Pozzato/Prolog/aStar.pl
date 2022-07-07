/*  
    ####################################
    Implementazione PROLOG algoritmo A*
    ####################################

    Descrizione:

    
    g(n) = costo minimo di tutti i percorsi visitati per raggiungere il nodo n da S (stato iniziale),
            dato che ogni ramo ha costo 1  -> g(n) = length(AzioniPerS),ossia la lunghezza della lista delle azioni fatte per
            arrivare a quel nodo.

    h(n) = Distanza di Manhattan, stima del costo minimo dal nodo attuale al goal. 
            Calcolata come abs(RigaNodo -  RigaGoal) + abs(ColonnaNodo - ColonnaGoal), dove abs() calcola il valore assoluto.
    f(n) = g(n) + h(n) euristica del costo minimo per raggiungere il gol da n 

    Bisogna tenere traccia dei costi f(n) precedenti: se si trova f(n) maggiore di quello appena analizzato,
     si torna ai genitori e si sceglie il min(f(n))

    Aggiunto elemento F (= euristica f(n)) al nodo -> 
    lo si sfrutta quando si riordina la coda dalla quale si andrà ad estrarre sempre il primo elemento,
    ovvero quello con F minore, grazie a quick sort.
    
    ####################################
    Authors:
    Bushaj Aldo
    Bushaj Antonino
    Frisullo Silvestro Stefano
    ####################################

*/
:- ['Labirinti/labirinto20x20.pl', 'azioni.pl','inversioneLista.pl','quick_sort.pl','heuristic.pl'].
a*(Soluzione):-
    iniziale(S),
    bfs([nodo(S,[],0)],[],Soluzione). % Acronimo che indica visita in ampiezza

bfs([nodo(S,AzioniPerS,_)|_],_,AzioniPerSInvertita):- 
    finale(S), 
    !,
    invertiOpt(AzioniPerS,AzioniPerSInvertita). % Inversione lista del cammino perchè le AZ vengono messe in testa e non in ocoda
   
 
bfs([nodo(S,AzioniPerS,F)|CodaStati],Visitati,Soluzione):-
   findall(Az, applicabile(Az,S), ListaAzApplicabili), % trova tutti i nodi figli
   generaStatiFigli(nodo(S,AzioniPerS,F),[S|Visitati],ListaAzApplicabili,StatiFigli), % espando il nodo 
   quick_sort2(CodaStati,StatiOrdered),

   
   append(StatiOrdered,StatiFigli,NuovaCoda),
   bfs(NuovaCoda,[S|Visitati],Soluzione).

generaStatiFigli(_,_,[], []).

generaStatiFigli(nodo(S,AzioniPerS,F),Visitati,[Az|AltreAzioni],[nodo(SNuovo,[Az|AzioniPerS],F)|AltriFigli]):-  
    trasforma(Az,S,SNuovo),
    % Calcolo dell'euristica per il nodo corrente
    heuristic(S,H),
    length(AzioniPerS,LenAzioniPerS), % g
    NF is H + LenAzioniPerS, % F = g+h
    \+member(SNuovo,Visitati), %se termina con insuccesso vado al rigo 65
    !,
    generaStatiFigli(nodo(S,AzioniPerS,NF),Visitati,AltreAzioni,AltriFigli).

% In questo caso se il nodo era già stato visitato ritorno al caso precedente senza fare niente
generaStatiFigli(nodo(S,AzioniPerS,_),Visitati,[_|AltreAzioni],AltriFigli):-
    heuristic(S,H),
    length(AzioniPerS,LenAzioniPerS),
    NF is H + LenAzioniPerS,  
    generaStatiFigli(nodo(S,AzioniPerS,NF),Visitati,AltreAzioni,AltriFigli).

/*
heuristic(pos(Riga,Col),H):-
    finale(pos(RFin,ColFin)),
    H is abs(Riga - RFin) + abs(Col - ColFin). */


