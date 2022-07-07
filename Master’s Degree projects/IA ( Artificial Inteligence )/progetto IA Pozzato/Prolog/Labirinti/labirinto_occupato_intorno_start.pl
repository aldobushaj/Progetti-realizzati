num_righe(10).
num_colonne(10).
iniziale(pos(4,2)).
finale(pos(7,9)).
%finale(pos(4,3)).
occupata(pos(7,1)).
occupata(pos(7,2)).
occupata(pos(7,3)).
occupata(pos(7,4)).
occupata(pos(7,5)).
occupata(pos(2,5)).
occupata(pos(3,5)).
occupata(pos(4,5)).
occupata(pos(5,5)).
occupata(pos(6,5)).
occupata(pos(4,7)).
occupata(pos(4,8)).
occupata(pos(4,9)).
occupata(pos(4,10)).
occupata(pos(5,7)).
occupata(pos(6,7)).
occupata(pos(7,7)).
occupata(pos(8,7)).

%No soluzione celle occupante intorno al nodo iniziale
occupata(pos(4,3)).%est
occupata(pos(3,2)).%nord
occupata(pos(5,2)).%sud
occupata(pos(4,1)). %ovest


% Celle attorno al goal: nessuna soluzione
/*occupata(pos(6,8)).
occupata(pos(6,9)).
occupata(pos(6,10)).
occupata(pos(7,8)).
occupata(pos(7,10)).
occupata(pos(8,8)).
occupata(pos(8,9)).
occupata(pos(8,10)).
*/