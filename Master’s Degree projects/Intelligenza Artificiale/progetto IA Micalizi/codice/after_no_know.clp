(defmodule AFTER_NO_KNOW (import MAIN ?ALL) (import ENV ?ALL) (import AGENT ?ALL)(export ?ALL))

;se la fire di heuristic va a buon fine cancella dalla kb il token precedentemente rilasciato 

(defrule after-heuristic-match (declare (salience 2))
    ?f <- (to-fire(x ?x)(y ?y))
    (k-cell (x ?x)(y ?y))
	(not(guessed(x ?x)(y ?y)))
    =>
    (retract ?f)
	(printout t "(after-heuristic-match)" crlf)
)

;se la fire di huristic non va a buon fine allora controlla se la riga a piu pezzi di nave 
;della colonna, quindi fa un controllo per evitare di uscire dai margini e rilascia un 
;token che servira a fare la fire

(defrule after-heuristic-no-match (declare (salience 2))
    ?f <- (to-fire(x ?r)(y ?c))
    (not(k-cell (x ?r)(y ?c)))
	(k-per-row(row ?r)(num ?n1))
	(k-per-col(col ?c)(num ?n3))
    =>
	(if (>= ?n1 ?n3)
	then 
		(if (>= ?c 8) then  ; mettere 9 se vuoi aumentare di 1
			(assert (move-y(x ?r)(y 0)))
			(printout t "((>= ?c 8)) " ?r " " ?c  crlf)
		else 
			(assert (move-y(x ?r)(y (+ ?c 2)))) ; mettere 1 se vuoi aumentare di 1
			(printout t "((< ?c 8))" ?r " "?c crlf)
		)
	else
		(if (>= ?r 8) then ; mettere 9 se vuoi aumentare di 1
			(assert (move-x(y ?c)(x 0)))
			(printout t "(>= ?r 8) " ?r " " ?c  crlf)
		else 
			(assert (move-x(y ?c)(x (+ ?r 2)))) ; mettere 1 se vuoi aumentare di 1
			(printout t "(< ?r 8) " ?r " " ?c   crlf)
		) 
	)
    (printout t "(after-heuristic-no-match)" crlf)
    (retract ?f)
)


(defrule after-heuristic-aleredy-matched (declare (salience 1))
    ?f <- (to-fire(x ?r)(y ?c))
	(k-per-row(row ?r)(num ?n1))
	(k-per-col(col ?c)(num ?n3))
    =>
	(if (>= ?n1 ?n3)
	then 
		(if (>= ?c 8) then  ; mettere 9 se vuoi aumentare di 1
			(assert (move-y(x ?r)(y 0)))
			(printout t "((>= ?c 8)) " ?r " " ?c  crlf)
		else 
			(assert (move-y(x ?r)(y (+ ?c 2)))) ; mettere 1 se vuoi aumentare di 1
			(printout t "((< ?c 8))" ?r " "?c crlf)
		)
	else
		(if (>= ?r 8) then ; mettere 9 se vuoi aumentare di 1
			(assert (move-x(y ?c)(x 0)))
			(printout t "(>= ?r 8) " ?r " " ?c  crlf)
		else 
			(assert (move-x(y ?c)(x (+ ?r 2)))) ; mettere 1 se vuoi aumentare di 1
			(printout t "(< ?r 8) " ?r " " ?c   crlf)
		) 
	)
    (printout t "(after-heuristic-aleredy-matched)" crlf)
    (retract ?f)
)


;se e presente il token move-y si attiva questa regola che controlla dove dovrebbe 
;essere fatta la fire e se non è gia stata scoperta e ci sono abbastanza 
;pezzi di nave sulla linea allora fa la fire, rilasciando il token to-fire 
;per ripetere da capo il coclo di controlli 


(defrule fire-move-y-ok 
	(moves (fires ?fires&:(> ?fires 0)))
	(status (step ?s)(currently running))
	?f<-(move-y (x ?x)(y ?y))
	(not(guessed(x ?x)(y ?y)))
	(k-per-col(col ?y)(num ?c-val))
	=>
	(if (> ?c-val 0) 
	then 
		(printout t "(fire-move-y-ok) fire in " ?x " " ?y crlf)
    	(assert(to-fire(x ?x)(y ?y)))
    	(assert(exec (step ?s) (action fire) (x ?x)(y ?y)))
		(assert(return))
        (pop-focus)
	else
		(printout t "(fire-move-y-ok) non ci sono abbastanzza k in " ?x " " ?y crlf)
		(assert(to-fire(x ?x)(y ?y)))
	)
	(retract ?f)
)

;come la precedente ma si attiva quando e stata guessata la posizione dove in 
;teoria si sarebbe divuta fare la fire quindi asserisce un to-fire per ripetere il ciclo

(defrule fire-move-y-guessed 
	(moves (fires ?fires&:(> ?fires 0)))
	?f<-(move-y (y ?y)(x ?x))
	(guessed(x ?x)(y ?y))
	=>
	(printout t "(fire-move-y-guessed) " ?x " " ?y crlf)
    (assert(to-fire(x ?x)(y ?y)))
	(retract ?f)
)

;come sopra ma per l asse y  

(defrule fire-move-x-ok 
	(moves (fires ?fires&:(> ?fires 0)))
	(status (step ?s)(currently running))
	?f<-(move-x (x ?x)(y ?y))
	(not(guessed(x ?x)(y ?y)))
	(k-per-row(row ?x)(num ?r-val))
	=>
	(if (> ?r-val 0) 
	then 
		(printout t "(fire-move-x-ok)" ?x  crlf)
    	(assert(to-fire(x ?x)(y ?y)))
    	(assert(exec (step ?s) (action fire) (x ?x)(y ?y)))
		(assert(return))
        (pop-focus)
	else
		(assert(to-fire(x ?x)(y ?y)))
	)
	(retract ?f)
)

;come sopra ma per l asse y  

(defrule fire-move-x-guessed 
	(moves (fires ?fires&:(> ?fires 0)))
	?f<-(move-x (x ?x)(y ?y))
	(guessed(x ?x)(y ?y))
	=>
	(printout t "(fire-move-x-guessed)" ?x  crlf)
    (assert(to-fire(x ?x)(y ?y)))
	(retract ?f)
)
