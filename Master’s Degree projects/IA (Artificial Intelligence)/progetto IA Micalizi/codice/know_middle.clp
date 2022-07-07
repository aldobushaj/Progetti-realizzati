(defmodule KNOW_MIDDLE (import MAIN ?ALL) (import ENV ?ALL) (import AGENT ?ALL)(export ?ALL))

;in questo modulo si trovano le regole che si attivano con la conoscienza di un middle 

(defrule know-middle-t-b-guessed 
    (status (step ?s)(currently running))
	(k-cell (x ?x)(y ?y)(content middle))
    (not(guessed (x ?x1&:(eq (+ ?x 1) ?x1))(y ?y)))
	(not(guessed (x ?xb&:(eq (- ?x 1) ?xb))(y ?y)))
    =>
	(printout t "(know-middle-t-b-guessed) x " ?x " y " ?y crlf)
    (assert(top-to-bottom (x ?x)(y ?y)))
)

(defrule know-middle-l-r-guessed 
    (status (step ?s)(currently running))
	(k-cell (x ?x)(y ?y)(content middle))
	(not(guessed (x ?x)(y ?y1 &:(eq (+ ?y 1) ?y1))))
	(not(guessed (x ?x)(y ?yb &:(eq (- ?y 1) ?yb))))
	(or (guessed (x ?x1&:(eq (+ ?x 1) ?x1))(y ?y))
	(guessed (x ?xb&:(eq (- ?x 1) ?xb))(y ?y)))
    =>
	(printout t "(know-middle-l-r-guessed) x " ?x " y " ?y crlf)
    (assert(left-to-right (x ?x)(y ?y)))
)

(defrule know-middle (declare(salience 1))
	(status (step ?s)(currently running))
	(k-cell (x ?x)(y ?y)(content middle))
	(not(guessed (x ?x)(y ?y)))
	(not(guessed (x ?x1&:(eq (+ ?x 1) ?x1))(y ?y)))
	(not(guessed (x ?xb&:(eq (- ?x 1) ?xb))(y ?y)))
	(not(guessed (x ?x)(y ?y1 &:(eq (+ ?y 1) ?y1))))
	(not(guessed (x ?x)(y ?yb &:(eq (- ?y 1) ?yb))))
	(k-per-row (row ?x) (num ?val-row1))
	(k-per-col (col ?y) (num ?val-col1))
    (g-per-row (num ?x) (val ?r))
    (g-per-col (num ?y) (val ?c))
    =>
    (if( >= ( + ?val-row1 ?r) (+ ?val-col1 ?c))
    then
		(assert(left-to-right (x ?x)(y ?y)))
        (printout t "(know-middle)righe maggiori di colonne " crlf)
    else
        (assert(top-to-bottom (x ?x)(y ?y)))
	;	(assert(middle-t-b (x ?x)(y ?y)))
        (printout t "(know-middle)colonne maggiori di righe" crlf)
    )
)

(defrule know-middle-fire-l-r (declare(salience 2))
    ?f <- (left-to-right (x ?x)(y ?y))
	(moves (fires ?fires&:(> ?fires 0)))
	(status (step ?s)(currently running))
	(k-per-col (col ?y1 &:(eq (+ ?y 1) ?y1)) (num ?val-col))
	(g-per-col (num ?gy1 &:(eq (+ ?y 1) ?gy1)) (val ?c))
	(k-per-col (col ?yb &:(eq (- ?y 1) ?yb)) (num ?val-col1))
	(g-per-col (num ?gyb &:(eq (- ?y 1) ?gyb)) (val ?c1))
	=>
	(retract ?f)
	(if (>= (+ ?c ?val-col) (+ ?c1 ?val-col1))
		then 
		(printout t "(know-middle-fire-l-r) right migliore di left middle in "?x" "?y  crlf)
		(assert(to-guess (x ?x)(y ?y1)))
		(assert(return))
        (assert(exec (step ?s) (action fire) (x ?x) (y ?y1)))
        (pop-focus)
		else
		(assert(exec (step ?s) (action fire) (x ?x) (y ?yb)))
		(printout t " (know-middle-fire-l-r) left migliore di right per middle in "?x" "?y crlf)
		(assert(to-guess (x ?x)(y ?yb)))
		(assert(return))
        (pop-focus)
	)
)



(defrule know-middle-fire-t-b (declare(salience 2))
    ?f <- (top-to-bottom (x ?x)(y ?y))
	(not(guessed (x ?x)(y ?y)))
	(moves (fires ?fires&:(> ?fires 0)))
	(status (step ?s)(currently running))
	(k-per-row (row ?x1 &:(eq (+ ?x 1) ?x1)) (num ?val-row))
	(g-per-row (num ?gx1 &:(eq (+ ?x 1) ?gx1)) (val ?r))
	(k-per-row (row ?xb &:(eq (- ?x 1) ?xb)) (num ?val-row1))
	(g-per-row (num ?gxb &:(eq (- ?x 1) ?gxb)) (val ?r1))
	=>
	(retract ?f)
	(if (>= (+ ?r ?val-row) (+ ?r1 ?val-row1))
		then 
		(assert(exec (step ?s) (action fire) (x ?x1) (y ?y)))
		(printout t "(know-middle-fire-t-b)bottom migliore di top " crlf)
		(assert(to-guess (x ?x1)(y ?y)))
		(assert(return))
        (pop-focus)
		else
		(assert(exec (step ?s) (action fire) (x ?xb) (y ?y)))
		(printout t "(know-middle-fire-t-b)top migliore di bottom" crlf)
		(assert(to-guess (x ?xb)(y ?y)))
		(assert(return))
        (pop-focus)
	)
)