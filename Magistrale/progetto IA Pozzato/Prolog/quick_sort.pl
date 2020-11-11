%quick sort 2 FUNZIONA
pivoting(_,[],[],[]).
pivoting(nodo(S,AzioniPerS, F_H),[nodo(SX,AzioniPerSX, F_X)|T],[nodo(SX,AzioniPerSX, F_X)|L],G):-
    F_X>F_H,
    pivoting(nodo(S,AzioniPerS, F_H),T,L,G).
pivoting(nodo(S,AzioniPerS, F_H),[nodo(SX,AzioniPerSX, F_X)|T],L,[nodo(SX,AzioniPerSX, F_X)|G]):-
    F_X=<F_H,
    pivoting(nodo(S,AzioniPerS, F_H),T,L,G).


quick_sort2(List,Sorted):-
    q_sort(List,[],Sorted).
q_sort([],Acc,Acc).
q_sort([H|T],Acc,Sorted):-
	pivoting(H,T,L1,L2),
    q_sort(L1,Acc,Sorted1),
    q_sort(L2,[H|Sorted1],Sorted).