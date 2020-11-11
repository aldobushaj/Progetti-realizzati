(defmodule MAIN_CONTROL (import MAIN ?ALL) (import ENV ?ALL) (import AGENT ?ALL)(export ?ALL))

;termina il programma e calcola il punteggio 
;ci troviamo nel caso in cui abbiamo trovato tutte le navi

(defrule end 
	(closed-boats(one 4)(two 3)(three 2)(four 1))
	(status (step ?s)(currently running))
	=>
	(printout t "(end)"  crlf)
	(assert (exec (step ?s) (action solve)))
    (assert(end))
	(focus ENV)
)

; stampa sul terminale caselle note all'inizio(scritto dal prof)

(defrule print-what-i-know-since-the-beginning
	(k-cell (x ?x) (y ?y) (content ?t) )
=>
	(printout t "I know that cell [" ?x ", " ?y "] contains " ?t "." crlf)
)
;non si puo fare piu di una guess per regola 
;usiamo questa funzione per poterlo fare

(defrule to-ausiliar-exec (declare (salience 3))
	?f <- (ausiliar-exec (action ?a) (x ?x)(y ?y))
	(status (step ?s)(currently running))
	=>
	(assert(exec (step ?s) (action guess) (x ?x) (y ?y )))
	(retract ?f)
	(assert(return))
    (pop-focus)
)

;asserisce le celle marcate con un to guess come guessate 
;se non escono dalla mappa e quindi sono inesistenti 
;e se non sono gia state guessate in precedenza

(defrule guesser (declare (salience 2))
	?f <- (to-guess(x ?x)(y ?y))
	(not (guessed(x ?x)(y ?y)))
	=>
	(if (and (< ?x 10)(< ?y 10)(>= ?y 0)(>= ?x 0))then 
		(assert(guessed (x ?x)(y ?y)))
    )
	(retract ?f)
)

;se non si attiva la regola sopra vuol dire che in wm ci sono dei fatti 
;to-guess che o sono stati gia guessati o sono fuori dalla mappa, questa 
;regola si limita a toglierli dalla wm

(defrule garbage-w-g (declare (salience 1))
	?f <- (to-guess (x ?x)(y ?y))
	=>
	(retract ?f)
)
