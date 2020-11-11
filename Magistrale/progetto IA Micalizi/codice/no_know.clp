(defmodule NO_KNOW (import MAIN ?ALL) (import ENV ?ALL) (import AGENT ?ALL)(export ?ALL))



(defrule end-two (declare (salience 1))
	(moves (fires 0))
	(status (step ?s)(currently running))
	=>
	(printout t "(end-two)"  crlf)
	(assert (exec (step ?s) (action solve)))
    (assert(end))
	(focus ENV)
)

; si attiva quando ci sono ancora fire a disposizione e non si conoscono pezzi
; quindi trova la riga e la colonna con maggiore numero di pezzi di nave e fa una fire
; dopodiche asserisce un token to-fire che serve per controllare 
; se la fire e andata a buon fine o meno

(defrule heuristic (declare (salience 1))
	(moves (fires ?fires&:(> ?fires 0)))
	(status (step ?s)(currently running))
	(k-per-row(row ?r)(num ?n1))
    (not (k-per-row (num ?n2&:(> ?n2 ?n1))))
	(k-per-col(col ?c)(num ?n3))
    (not (k-per-col (num ?n4&:(> ?n4 ?n3))))
	(not(guessed (x ?r)(y ?c)))
    =>
	(printout t "(heuristic)" ?r " " ?c crlf)
    (assert(to-fire(x ?r)(y ?c)))
    (assert(exec (step ?s) (action fire) (x ?r)(y ?c)))
    (assert(return))
	(pop-focus)
)


; ha la stessa parte sinistra di heuristic a parte l ultima riga che per heuristic è 
; (not(guessed (x ?r)(y ?c))) invece per heuristic-two è (guessed (x ?r)(y ?c))
; che vuol dire che la casella con quella riga e quella colonna e già stata scoperta 
; quindi si comporta come after-heuristic-no-match

(defrule heuristic-two 
	(status (step ?s)(currently running))
	(moves (fires ?fires&:(> ?fires 0)))
	(k-per-row(row ?r)(num ?n1))
    (not (k-per-row (num ?n2&:(> ?n2 ?n1))))
	(k-per-col(col ?c)(num ?n3))
    (not (k-per-col (num ?n4&:(> ?n4 ?n3))))
	(guessed (x ?r)(y ?c))
    =>
	(if (>= ?n1 ?n3)
	then 
		(if (>= ?c 8) then  ; mettere 9 se vuoi aumentare di 1
			(assert (move-y(x ?r)(y 0)))
			(printout t "((>= ?c 8))" ?r " " ?c  crlf)
		else 
			(assert (move-y(x ?r)(y (+ ?c 2)))) ; mettere 1 se vuoi aumentare di 1
			(printout t "((< ?c 8))" ?r " " ?c crlf)
		)
	else
		(if (>= ?r 8) then ; mettere 9 se vuoi aumentare di 1
			(assert (move-x(y ?c)(x 0)))
			(printout t "(>= ?r 8)" ?r " " ?c crlf)
		else 
			(assert (move-x(y ?c)(x (+ ?r 2)))) ; mettere 1 se vuoi aumentare di 1
			(printout t "(< ?r 8)"  ?r " " ?c crlf)
		) 
	)
	(printout t "(heuristic-two)" crlf)
)
