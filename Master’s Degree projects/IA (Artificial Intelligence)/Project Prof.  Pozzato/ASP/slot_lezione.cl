%*
ORE TOTALI DI LEZIONE: 364 (2 PRESENTAZIONE + 358 DI MATERIE + 4 ORE PER EVENTUALI RECUPERI)

ORE MINIME CON  SABATO DA 4 ORE: 352
ORE MAX CON SABATO DA 5: 376  

12 SABATI DA 5 ORE




*%
%###############################
%FATTI
%###############################
%materia disponibile
materia(presentazione).
materia(recupero).
materia(proj_man).            % Project Management
materia(ict_paradig_prog).    % Fondamenti di ICT e paradigmi di programazione
materia(ling_markup).         % Linguaggi di Markup
materia(gest_qual).           % Gestione della Qualiatà
materia(amb_svil_lcs).        % Ambienti di sviluppo e Linguaggi Clien-side per il web
materia(prog_graf_des_int).   % Progettazione Grafica e Design Interfacce
materia(prog_basi_dati).      % Progettazione Basi di Dati
materia(str_met_social).      % Strumenti e Metodi di interazione nei social media
materia(ac_elab_img).         % Acquisizione ed Elaborazione di Immagine Statiche-grafica
materia(acc_usb_prog_mult).   % Accessibilità e usabilità nella progettazione multimediale
materia(marketing_dig).       % Marketing Digitale
materia(foto_dig).            % Elementi di Fotografia Digitale
materia(ris_prog_coll_doc).   % Risorse digitali per il progetto collaborazione e documentazione
materia(tec_server_side).     % Tecnologie Server_side per il web
materia(tecn_str_mark).       % Tecniche e Strumenti di Marketing Digitale
materia(social_media_man).    % Introduzione al Social Media Management
materia(acq_elab_suono).      % Acquisizione ed elaborazione del suono
materia(acq_elab_seq_img).    % Acquisizione ed elaborazione di sequenze di Immagini Digitali
materia(com_pub).             % Comunicazione pubblicitaria e comunicazione pubblica
materia(semiol_multim).       % Semiologia e multimedialità
materia(cross_media).         % Crossmedia articolazione delle scritture multimediali
materia(grafica_3D).          % Grafica 3D
materia(prog_svlp_disp_mob).  % Progettazione e sviluppo di applicazioni web su dispositivi mobili 1
materia(prog_svlp_disp_mob_2).% Progettazione e sviluppo di applicazioni web su dispositivi mobili 2
materia(ris_umane).           % Gestione delle Risorse Umane
materia(vinc_giur_media).     % Vincoli giuridici del progetto diritto dei media



%DOCENTI
docente(presentatore).
docente(supplente).
docente(muzzetto).
docente(pozzato).
docente(gena).
docente(tomatis).
docente(micalizio).
docente(terranova).
docente(mazzei).
docente(giordani).
docente(zanchetta).
docente(vargiu).
docente(boniolo).
docente(damiano).
docente(suppini).
docente(valle).
docente(ghidelli).
docente(gabardi).
docente(santangelo).
docente(taddeo).
docente(girbaudo).
docente(schifanella).
docente(lombardo).
docente(travostino).



%piu giorni della settimana disponibili
giorno(1..6).

%orari disponibili
ora(1..8).

%settimane
settimana(1..24).


%Docente-Materia

insegna(presentatore,presentazione).

insegna(supplente,recupero).

insegna(muzzetto,proj_man).
insegna(pozzato,ict_paradig_prog).
insegna(gena,ling_markup).
insegna(tomatis,gest_qual).
insegna(micalizio,amb_svil_lcs).
insegna(terranova,prog_graf_des_int).
insegna(mazzei,prog_basi_dati).
insegna(giordani,str_met_social).
insegna(zanchetta,ac_elab_img).
insegna(gena,acc_usb_prog_mult).
insegna(muzzetto,marketing_dig).
insegna(vargiu,foto_dig).
insegna(boniolo,ris_prog_coll_doc).
insegna(damiano,tec_server_side).
insegna(zanchetta,tecn_str_mark).
insegna(suppini,social_media_man).
insegna(valle,acq_elab_suono).
insegna(ghidelli,acq_elab_seq_img).
insegna(gabardi,com_pub).
insegna(santangelo,semiol_multim).
insegna(taddeo,cross_media).
insegna(girbaudo,grafica_3D).
insegna(pozzato,prog_svlp_disp_mob).
insegna(schifanella,prog_svlp_disp_mob_2).
insegna(lombardo,ris_umane).
insegna(travostino,vinc_giur_media).

% Lezione Lun-Giov solo settimana 7 e 16
slot_lezione(M,S,G,X,D):- 
  insegna(D,M),
  settimana(S),
  giorno(G),
  ora(X),
  G<5,
  S==7, 
  si_svolge(M,S,G,X).

slot_lezione(M,S,G,X,D):- 
  insegna(D,M),
  settimana(S),
  giorno(G),
  ora(X),
  G<5,
  S==16, 
  si_svolge(M,S,G,X).

% Il sabato lezione fino alla 5 ora , sempre
slot_lezione(M,S,G,X,D):- 
  insegna(D,M),
  settimana(S),
  giorno(G),
  ora(X),
  G== 6,
  X<6,
  si_svolge(M,S,G,X).

% Il venerdi lezione di 8 ore, sempre
slot_lezione(M,S,G,X,D):- 
  insegna(D,M),
  settimana(S),
  giorno(G),
  ora(X),
  G==5,
  si_svolge(M,S,G,X).

idOra(S, G, O, ID) :-
    settimana(S), giorno(G), ora(O),
    ID = (S*100) + (G * 10) + O. % peso 100 alla settimana, 10 al giorno e 1 all'ora
    %ID = (S - 1) * 48 + (G - 1) * 8 + O.


%###############################
%REGOLE
%###############################

%In un'ora si svolge max una lezione
{ si_svolge(M,S, G, X) : materia(M) } 1 :- settimana(S),giorno(G), ora(X). %OK

%############# VINCOLI RIGIDI #################
%ORE TOTALI PER MATERIA

%Numero ore per materia
:- #count{O,S,G,D: slot_lezione(proj_man,S,G,O,D)} != 14.  
:- #count{O,S,G,D: slot_lezione(ict_paradig_prog,S,G,O,D)} != 14.  
:- #count{O,S,G,D: slot_lezione(ling_markup,S,G,O,D)} != 20.  
:- #count{O,S,G,D: slot_lezione(gest_qual,S,G,O,D)} != 10.
:- #count{O,S,G,D: slot_lezione(amb_svil_lcs,S,G,O,D)} != 20.
:- #count{O,S,G,D: slot_lezione(prog_graf_des_int,S,G,O,D)} != 10.
:- #count{O,S,G,D: slot_lezione(prog_basi_dati,S,G,O,D)} != 20.
:- #count{O,S,G,D: slot_lezione(str_met_social,S,G,O,D)} != 14.
:- #count{O,S,G,D: slot_lezione(ac_elab_img,S,G,O,D)} != 14.
:- #count{O,S,G,D: slot_lezione(acc_usb_prog_mult,S,G,O,D)} != 14.
:- #count{O,S,G,D: slot_lezione(marketing_dig,S,G,O,D)} != 10.
:- #count{O,S,G,D: slot_lezione(foto_dig,S,G,O,D)} != 10.
:- #count{O,S,G,D: slot_lezione(ris_prog_coll_doc,S,G,O,D)} != 10.
:- #count{O,S,G,D: slot_lezione(tec_server_side,S,G,O,D)} != 20.
:- #count{O,S,G,D: slot_lezione(tecn_str_mark,S,G,O,D)} != 10.
:- #count{O,S,G,D: slot_lezione(social_media_man,S,G,O,D)} != 14.
:- #count{O,S,G,D: slot_lezione(acq_elab_suono,S,G,O,D)} != 10.
:- #count{O,S,G,D: slot_lezione(acq_elab_seq_img,S,G,O,D)} != 20.
:- #count{O,S,G,D: slot_lezione(com_pub,S,G,O,D)} != 14.
:- #count{O,S,G,D: slot_lezione(semiol_multim,S,G,O,D)} != 10.
:- #count{O,S,G,D: slot_lezione(cross_media,S,G,O,D)} != 20.
:- #count{O,S,G,D: slot_lezione(grafica_3D,S,G,O,D)} != 20.
:- #count{O,S,G,D: slot_lezione(prog_svlp_disp_mob,S,G,O,D)} != 10.
:- #count{O,S,G,D: slot_lezione(ris_umane,S,G,O,D)} != 10.
:- #count{O,S,G,D: slot_lezione(vinc_giur_media,S,G,O,D)} != 10.
:- #count{O,S,G,D: slot_lezione(prog_svlp_disp_mob_2,S,G,O,D)} != 10.

% almeno 4 ore di recupero totali
:- #count{O,S,G,D: slot_lezione(recupero,S,G,O,D)} < 4.  


% 1- MAX 4) ORE AL GIORNO DI LEZIONE PER OGNI PROF
:-
  slot_lezione(_,S,G,_,D),
  #count{X: slot_lezione(M,S,G,X,D) } >4. 

%2- MIN 2 E MAX 4 ORE STESSO GIORNO PER MATERIA
:-
  slot_lezione(M,S,G,_,D),
  #count{X: slot_lezione(M,S,G,X,D) } <2. 
:-
  slot_lezione(M,S,G,_,D),
  #count{X: slot_lezione(M,S,G,X,D) } >4.  

%I blocchi di recupero  
%Non può esistere un O < O2 e sommatogli 1 non sia uguale a O2.
%non può essere che:(slot1 and slot2 and O < O2 and non_sono_consecutivi ). -> se è true non va bene, quindi deve esserer false

:- slot_lezione(recupero, S, G, O,supplente), slot_lezione(recupero, S, G, O2,supplente), O < O2, O+1 != O2.  



% ALTRI VINCOLI 

% Esattamente 8 ore il venerdi
%:- slot_lezione(_,S,5,_,_),#count{O,D: slot_lezione(_,S,5,O,D)} != 8. 

% Min 4 ore il sabato
%:- slot_lezione(_,S,6,_,_),#count{O,D: slot_lezione(_,S,6,O,D) } <4.  

% Max 5 ore il sabato
%:- slot_lezione(_,S,6,_,_),#count{O,D: slot_lezione(_,S,6,O,D)} >5 . 

% dopo la 7 settimana il numero di ore di proj_man è 0, quindi va completato entro 7 settimana
:- slot_lezione(proj_man,S1,_,_,_),#count{O,S,G,D: slot_lezione(proj_man,S,G,O,D),S>7} != 0 . 


% settimaan 7  8 ore al giorno da lun a giovedì
%:- slot_lezione(_,7,G,_,_),#count{O,G,D: slot_lezione(_,7,G,O,D),G<5} != 8. 

%*
% settimaan 16  8 ore al giorno da lun a giovedì
:- slot_lezione(_,16,G,_,_),#count{O,G,D: slot_lezione(_,16,G,O,D),G<5} != 8. 
*%

%:-  #count{O,S,G,D: slot_lezione(_,S,G,O,D), G<5, S!= 16} !=0. % no lezioni in altri giorni tranne settimana 16 e 7

%*-------------------------------------------------------------------------- 
La prima lezione dell’insegnamento “Accessibilità e usabilità nella
% progettazione multimediale” deve essere collocata prima che siano
terminate le lezioni dell’insegnamento “Linguaggi di markup”
--------------------------------------------------------------------------
*%
:- 
  slot_lezione(acc_usb_prog_mult,S,G,O,D),
  slot_lezione(ling_markup,S1,G1,O1,D1),
  #count{O,S,G,D: slot_lezione(acc_usb_prog_mult,S,G,O,D)} ==0,
  #count{O1,S1,G1,D1: slot_lezione(ling_markup,S1,G1,O1,D1)} ==20.

%*:- ALTERNATIVA --da rivedere
  settimana(S), giorno(G), ora  (O), slot_lezione(acc_usb_prog_mult, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(ling_markup, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  #count{ ID:idOra(S, G, O, ID), idOra(S1, G1, O1, ID1),   ID > ID1} <1.
*%


%*--------------------------------------------------------------------------
            PROPEDEUTICITÀ
--------------------------------------------------------------------------
*%

:- 
  settimana(S), giorno(G), ora(O), slot_lezione(ict_paradig_prog, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(amb_svil_lcs, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  ID > ID1 .
:- 
  settimana(S), giorno(G), ora(O), slot_lezione(amb_svil_lcs, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(prog_svlp_disp_mob, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  ID > ID1 .
:- 
  settimana(S), giorno(G), ora(O), slot_lezione(prog_svlp_disp_mob, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(prog_svlp_disp_mob_2, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  ID > ID1 .
:- 
  settimana(S), giorno(G), ora(O), slot_lezione(prog_basi_dati, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(tec_server_side, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  ID > ID1 .
:- 
  settimana(S), giorno(G), ora(O), slot_lezione(ling_markup, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(amb_svil_lcs, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  ID > ID1 .
:- 
  settimana(S), giorno(G), ora(O), slot_lezione(proj_man, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(marketing_dig, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  ID > ID1 .
:- 
  settimana(S), giorno(G), ora(O), slot_lezione(marketing_dig, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(tecn_str_mark, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  ID > ID1 .
:- 
  settimana(S), giorno(G), ora(O), slot_lezione(proj_man, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(str_met_social, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  ID > ID1 .
:- 
  settimana(S), giorno(G), ora(O), slot_lezione(proj_man, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(prog_graf_des_int, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  ID > ID1 .

:- 
  settimana(S), giorno(G), ora(O), slot_lezione(ac_elab_img, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(foto_dig, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  ID > ID1 .

:- 
  settimana(S), giorno(G), ora(O), slot_lezione(foto_dig, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(acq_elab_seq_img, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  ID > ID1 .
:- 
  settimana(S), giorno(G), ora(O), slot_lezione(ac_elab_img, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(grafica_3D, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  ID > ID1 .

%*
--------------------------------------------------------------------------
                      VINCOLI AUSPICABILI
--------------------------------------------------------------------------
*% 

%la distanza tra la prima e l’ultima lezione di ciascun insegnamento non deve superare le 6 settimane
:- slot_lezione(M, S,_,_,D), slot_lezione(M, S2, _, _,D), S<S2, (S2-S) > 6.  

%*--------------------------------------------------------------------------
La prima lezione degli insegnamenti “Crossmedia: articolazione delle
scritture multimediali” e “Introduzione al social media management”
devono essere collocate nella seconda settimana full-time
--------------------------------------------------------------------------
*%
:- slot_lezione(cross_media, S,_,_,D), S<16, #count{O,S,G: slot_lezione(cross_media,S,G,O,D),S==16 } ==0.
:- slot_lezione(social_media_man, S,_,_,D), S<16, #count{O,S,G: slot_lezione(social_media_man,S,G,O,D),S==16 } ==0.



%*--------------------------------------------------------------------------
 Le lezioni dei vari insegnamenti devono rispettare le seguenti
propedeuticità, in particolare la prima lezione dell’insegnamento della
colonna di destra deve essere successiva alle prime 4 ore di lezione del
corrispondente insegnamento della colonna di sinistra:
--------------------------------------------------------------------------
*%
:- 
  slot_lezione(ict_paradig_prog, S,_,_,D),
  #count{O,S,G: slot_lezione(ict_paradig_prog,S,G,O,D)} < 4,
  #count{O1,S1,G1: slot_lezione(prog_basi_dati,S1,G1,O1,D1)} > 0.  


:- 
  slot_lezione(tecn_str_mark, S,_,_,D),
  #count{O,S,G: slot_lezione(tecn_str_mark,S,G,O,D)} < 4,
  #count{O1,S1,G1: slot_lezione(social_media_man,S1,G1,O1,D1)} > 0.  

:- 
  slot_lezione(com_pub, S,_,_,D),
  #count{O,S,G: slot_lezione(com_pub,S,G,O,D)} < 4,
  #count{O1,S1,G1: slot_lezione(social_media_man,S1,G1,O1,D1)} > 0. 

:- 
  slot_lezione(tec_server_side, S,_,_,D),
  #count{O,S,G: slot_lezione(tec_server_side,S,G,O,D)} < 4,
  #count{O1,S1,G1: slot_lezione(prog_svlp_disp_mob,S1,G1,O1,D1)} > 0. 

%*--------------------------------------------------------------------------
La distanza fra l’ultima lezione di “Progettazione e sviluppo di applicazioni
web su dispositivi mobile I” e la prima di “Progettazione e sviluppo di
applicazioni web su dispositivi mobile II” non deve superare le due
settimane.
--------------------------------------------------------------------------
*%

:- 
   settimana(S), giorno(G), ora(O), slot_lezione(prog_svlp_disp_mob, S, G, O,D), idOra(S, G, O, ID), 
  settimana(S1), giorno(G1), ora(O1), slot_lezione(prog_svlp_disp_mob_2, S1, G1, O1,D1), idOra(S1, G1, O1, ID1),
  (ID1-ID) > 200.
 



goal :- 
     
%*
--------------------------------------------------------------------------
ll primo giorno di lezione prevede che, nelle prime due ore, vi sia la
presentazione del master
--------------------------------------------------------------------------
*%
slot_lezione(presentazione,1,5,1,D), %  prima ora presentazione
slot_lezione(presentazione,1,5,2,D), % seconda ora presentazione
#count{O,S,G: slot_lezione(presentazione,S,G,O,D),settimana(S),giorno(G),docente(D),ora(O)} == 2. % esattamente 2 ore di presentazione




:- not goal.

#show slot_lezione/5.


