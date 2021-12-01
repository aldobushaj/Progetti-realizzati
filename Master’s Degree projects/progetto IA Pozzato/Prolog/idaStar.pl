
:- ['Labirinti/labirinto20x20.pl', 'azioni.pl', 'heuristic.pl'].


% ###################################################
% IDA* algorithm.
% Node is represent by ida_node/2 predicate and its structure is:
%   • NewS, that represent the node configuration,
%   • FNewS, that represent the F-value for the specific node.
% ###################################################
% Euristica H(n) = distanza stimata dal nodo N alla fine (distanza di Manhattan)
% G(n) = costo del cammino dall'inizio al nodo N
% Euristica F(n) = G+ H => Threshold 
% Threshold dello start = F(n)=G(n)+H(n)=0+H(n).

ida(Soluzione):-
    iniziale(S),
    heuristic(S,ThresholdIniziale), %calcolo F(n) di S
    idastar(S, Soluzione, [S], 0, ThresholdIniziale).
    %write("\n"), write(Soluzione), length(Soluzione,X).

idastar(S, Soluzione, Visitati, CostoCamminoS, Threshold):-
    %Ricerca
    ida_search(S, Soluzione, Visitati, CostoCamminoS, Threshold);
    
    %Aggiorna Threshold
    findall(FS, ida_node(_, FS), ListaThreshold),% prende tutti i valori F=g+h dei nodi e li salva in ListaTreshold
    exclude(>=(Threshold), ListaThreshold, ListaMaggioriThreshold), %prende da Listatreshold quei valori maggiori o uguali a Treshold e li salva in ListaMaggioriThreshold
    sort(ListaMaggioriThreshold, ListaOrdinata), %ordina la lista 
    nth0(0, ListaOrdinata, NewThreshold), %prende l'elemento in posizione 0 dalla lista ordinata
    /*write("h e "),
    write(Threshold),
    write("\n"),*/
    retractall(ida_node(_, _)), %ritratta i fatti sui nodi
    idastar(S, Soluzione, Visitati, 0, NewThreshold). %richiama la ricerca con la nuova Threshold ricominciando dallo start

% ###################################################
% ida_search/5 predicate provides the IDA* search.
% ###################################################
ida_search(S, [], _, _, _):-
    finale(S).
ida_search(S, [Az|AltreAzioni], Visitati, CostoCamminoS, Threshold):-
    applicabile(Az, S), %controlla che si possaq
    trasforma(Az, S, NewS), 
    \+member(NewS, Visitati),
    %cost(S, NewS, Cost),
    %Cost is 1,
    NuovoCostoCamminoS is CostoCamminoS + 1, %G(n)
    heuristic(NewS,EuristicaNuovoS), %h(n)
    FNewS is NuovoCostoCamminoS + EuristicaNuovoS, %f(n)=g(n)+h(n)
    assert(ida_node(NewS, FNewS)), % salva F(n) per il nodo
	FNewS =< Threshold,% perche?
    /*write("F e "),
    write(FNewS),
    write("\n"),*/
    ida_search(NewS, AltreAzioni, [NewS|Visitati], NuovoCostoCamminoS, Threshold).
