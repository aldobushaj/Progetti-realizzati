% 5x5 labyrinth

num_righe(5).
num_colonne(5).

iniziale(pos(1,1)).
finale(pos(1,4)).

%no solu
occupata(pos(1,3)).
occupata(pos(2,1)).
occupata(pos(2,2)).
occupata(pos(2,3)).
occupata(pos(2,4)).






occupata(pos(3,2)).
occupata(pos(3,3)).


%Più uscite
%finalPosition(pos(2,1)).

%Start bloccato
%occupata(pos(2,1)).

%Goal bloccato
/*
occupata(pos(5,3)).
occupata(pos(5,5)).
occupata(pos(4,4)).
occupata(pos(6,4)).
*/

/*
##
 S##
 ##

   E
*/