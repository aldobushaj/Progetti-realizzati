; Definisco un nuovo modulo "AGENT" importando tutto dai moduli "MAIN" e "ENV", quindi esporto tutto all'esterno
(defmodule AGENT (import MAIN ?ALL) (import ENV ?ALL) (export ?ALL))

; Costrutto utilizzato in modo da ricordare quante navi complete il sistema pensa di avere trovato,
; nel caso in cui queste siano rispettivamente 4,3,2,1 allora il sistema termina in quanto sono state trovate tutte le navi(come da consegna del progetto)
(deftemplate closed-boats
	(slot one (default 0)(range 0 4))
	(slot two (default 0)(range 0 3))
	(slot three (default 0)(range 0 2))
	(slot four (default 0)(range 0 1))
)
; identifica celle sulle quali bisogna eseguire un'azione di guess, grazie a questo costrutto si attiva la regola guesser che trasformerà le celle to-guess in guessed
; meccanismo utile a circoscrivere le celle attorno una nave(ogni nave deve essere circondata da acqua), verranno contrassegnate come "guessed" anche le navi stesse
(deftemplate to-guess
	(slot x)
	(slot y)
)
; identifica celle su cui è già stato fatto un guess
(deftemplate guessed
	(slot x)
	(slot y)
)
; permette di fare più guess per regola, ad esempio devo fare la guess di un "top" della nave, ma so già anche dove sono il "middle" e il "bot", allora procederò così:
; faccio la exec(regola scritta dal prof) del "top" mentre per il "middle" e il "bot" asserirò per ognuno un fatto ausiliar-exec che succesivamente farà la exec 
; delle coordinate specificate
(deftemplate ausiliar-exec
	(slot action)
	(slot x)
	(slot y)
)
; viene usato solo in know_one_indecision.clp, apparentemente non serve a niente, dobbiamo provare a commentare questa parte e vedere se funzina comunque
(deftemplate aux
	(slot x)
	(slot y)
	(slot content)
)
; guessed per colonna
(deftemplate g-per-col
    (slot num (range 0 9))
    (slot val (default 0)(range 0 9))
)
; guessed per riga
(deftemplate g-per-row
    (slot num (range 0 9))
    (slot val (default 0)(range 0 9))
)

(deftemplate top-to-bottom 
	(slot x)
	(slot y)
)

(deftemplate left-to-right 
	(slot x)
	(slot y)
)

(deftemplate to-fire
    (slot x)
    (slot y)
)

(deftemplate move-x
	(slot x)
	(slot y)
)

(deftemplate move-y
	(slot y)
	(slot x)
)

(deftemplate middle-l-r 
	(slot x)
	(slot y)
)

(deftemplate middle-t-b
	(slot x)
	(slot y)
)

(deftemplate limitate
	(slot x)
	(slot y)
)

; (deffacts table )

(deffacts init
(closed-boats) 
(g-per-col(num 0))
(g-per-col(num 1))
(g-per-col(num 2))
(g-per-col(num 3))
(g-per-col(num 4))
(g-per-col(num 5))
(g-per-col(num 6))
(g-per-col(num 7))
(g-per-col(num 8))
(g-per-col(num 9))
(g-per-row(num 0))
(g-per-row(num 1))
(g-per-row(num 2))
(g-per-row(num 3))
(g-per-row(num 4))
(g-per-row(num 5))
(g-per-row(num 6))
(g-per-row(num 7))
(g-per-row(num 8))
(g-per-row(num 9))
(phases MAIN_CONTROL AFTER_NO_KNOW KNOW_DOUBLE_MIDDLE KNOWTWO KNOWONE KNOWONE_INDECISION KNOW_MIDDLE NO_KNOW )
)

; Regola che ordina le fasi e quando si arriva alla fine le fa ricominciare dall inizio

(defrule pop 
    (not(end))
	; ogni regola in know_one, know_two ecc. alla fine asserisce un fatto return in modo da attivare questa regola, la quale farà il pop e darà nuovamente il focus
	; al MAIN_CONTROL, così facendo si eseguira nuovamente il ciclo per cercare un altro pezzo di nave seguendo i vari casi
    ?f <- (return) 
    ?c <- (phases $?a)
    =>
    (printout t "(----------------------pop--------------------) "crlf)
    (retract ?f)
    (retract ?c)
	; non c'è il file AFTER_NO_KNOW, serve questa fase?
    (assert(phases MAIN_CONTROL AFTER_NO_KNOW KNOW_DOUBLE_MIDDLE KNOWTWO KNOWONE KNOWONE_INDECISION KNOW_MIDDLE NO_KNOW ))
    (pop-focus)
)

; Regola che fa avanzare opportunamente le fasi mettendo quella corrente in focus( in esecuzione ) 

(defrule change-phase
    (not(end))
	?list <- (phases ?next-phase $?other-phases)
=>
	(focus ?next-phase)
    (printout t "(change-phase) " ?next-phase crlf)
	(retract ?list)
	(assert (phases $?other-phases ?next-phase))
)









