:-['Labirinti/labirinto20x20.pl','azioni.pl'].
% Profondità con una profondità limitata
:-dynamic(limite/1).
limite(0).
test(Soluzione):-
    limite(X),
    provaPL(Soluzione,X);
    limite(X), %se togliamo questo riga da warning Singleton variable in branch: X, ma funziona lo stesso
    NuovaX is X + 1,
    num_righe(A),
    num_colonne(B),
    NuovaX < A*B,
    retract(limite(_)),
    assert(limite(NuovaX)),
    
    test(Soluzione).
    


provaPL(Soluzione,Limite):-
    iniziale(S),
    ricerca_depth_limitata(S,[],Limite,Soluzione).

ricerca_depth_limitata(S,_,_,[]):-finale(S),!.
ricerca_depth_limitata(S,Visitati,Limite,[Az|SequenzaAzioni]):-
    Limite>0,
    applicabile(Az,S),
    trasforma(Az,S,SNuovo),
    \+member(SNuovo,Visitati),
    NuovoLimite is Limite-1,
    ricerca_depth_limitata(SNuovo,[S|Visitati],NuovoLimite,SequenzaAzioni).
    
