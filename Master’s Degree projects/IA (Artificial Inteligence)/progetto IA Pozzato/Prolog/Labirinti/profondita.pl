% prova(Soluzione)
prova(Soluzione):-
    iniziale(S),
    ricerca_depth(S,[S],Soluzione).

ricerca_depth(S,_,[]):-
    finale(S),!.
ricerca_depth(S,Visitati,[Az|SeqAzioni]):-
    applicabile(Az,S),
    trasforma(Az,S,SNuovo),
    \+member(SNuovo,Visitati),
    ricerca_depth(SNuovo,[SNuovo|Visitati],SeqAzioni).