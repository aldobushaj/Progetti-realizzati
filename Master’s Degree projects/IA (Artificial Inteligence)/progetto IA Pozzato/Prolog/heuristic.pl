% Euristica
heuristic(pos(Riga,Col),H):-
    finale(pos(RFin,ColFin)),
    H is abs(Riga - RFin) + abs(Col - ColFin).