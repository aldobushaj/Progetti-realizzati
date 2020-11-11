(defmodule KNOWONE_INDECISION (import MAIN ?ALL) (import ENV ?ALL) (import AGENT ?ALL)(export ?ALL))

;in questo modulo si trovano tutte le regole che si attivano conoscendo 
;la posizione di un estremo ma non sapendo la lunghezza della nave

; questa è la regola di indecisione per il top, faccio la fire 2 posizioni sotto in modo da vedere(supponende sia in verticale) quanto è grande la nave

(defrule indecision-top-one (declare (salience 1))
    (status (step ?s)(currently running))
	(moves (fires ?fires&:(> ?fires 0)))
	(k-cell (x ?x) (y ?y) (content top))
    (test(< (+ ?x 2) 10))
	(not(guessed (x ?x)(y ?y)))
    (not(guessed (x ?x2 &:(eq (+ ?x 2) ?x2))(y ?y)))
    ?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?row3 <- (k-per-row (row ?x2&:(eq (+ ?x 2) ?x2)) (num ?val-row3))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
    (test (and (>= ?val-col1 3) (>= ?val-row3 1)))
    =>
    (assert (aux (x ?x)(y ?y)(content top)))
	(assert(exec (step ?s) (action fire) (x (+ ?x 2))(y ?y)))
	(printout t "(indecision-top-one) x "?x " y " ?y crlf)
	(assert(return))
    (pop-focus)
)

; soluzione che si attiva se non si è attivata quella di sopra
; cioè la casella che si trova a top + 3 è guessata o si trova
; al di fuori della mappa

; in questo caso ho trovato una nave di grandezza 2
(defrule indecision-top-two 
    (status (step ?s)(currently running))
	(k-cell (x ?x) (y ?y) (content top))
    (not(guessed (x ?x)(y ?y)))
    (closed-boats (two ?t0-val&~3)) ; le navi di grandezza 2 sono solo 3, quindi non possono essere state trovate tutte in questa regola
    ?boats <- (closed-boats)
	?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?row2 <- (k-per-row (row ?x1&:(eq (+ ?x 1) ?x1)) (num ?val-row2))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
    =>
    (modify ?boats(two (+ ?t0-val 1)))
	(modify ?col1(num (- ?val-col1 2)))
	(modify ?row1(num (- ?val-row1 1)))
	(modify ?row2(num (- ?val-row2 1)))
	(assert(to-guess (x ?x)(y ?y)))
	(assert(to-guess (x ?x1)(y ?y)))
	(assert(to-guess (x (- ?x 1))(y ?y)))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
	(assert(to-guess (x ?x)(y (+ ?y 1))))
	(assert(to-guess (x ?x)(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 2))(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 2))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 2))(y ?y)))
	(assert(exec (step ?s) (action guess) (x (+ ?x 1))(y ?y)))
	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
	(printout t " (indecision-top-two) guess in x " ?x1 " y " ?y crlf)
	(assert(return))
    (pop-focus)
)

; regola che gestisce il caso in cui sia stata trovata una nave di lunghezza 4
(defrule to-indecision-top-find-middle  (declare (salience 2))
	(status (step ?s)(currently running))
	?f <-(aux (x ?x)(y ?y)(content top))
	;se due caselle sotto il "top" c'è un "middle" sono sicuro che sono nel caso di una nave di lunghezza 4

	;questo componente non poteva esserci sicuramente prima perche deve esserci dell acqua per far attivare questa regola (scritto da fede questo rigo)
	(k-cell (x ?x2 &:(eq (+ ?x 2) ?x2))(y ?y)(content middle))
	?boats <- (closed-boats (four 0))
	?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?row2 <- (k-per-row (row ?x1&: (eq (+ ?x 1) ?x1)) (num ?val-row2))
	?row3 <- (k-per-row (row ?x2) (num ?val-row3))
	?row4 <- (k-per-row (row ?x3&: (eq (+ ?x 3) ?x3)) (num ?val-row4))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
	=>
	(modify ?boats(four 1))
	(modify ?col1(num (- ?val-col1 4)))
	(modify ?row1(num (- ?val-row1 1)))
	(modify ?row2(num (- ?val-row2 1)))
	(modify ?row3(num (- ?val-row3 1)))
	(modify ?row4(num (- ?val-row4 1)))
	(assert(to-guess(x ?x) (y ?y)))
	(assert(to-guess(x ?x1) (y ?y)))
	(assert(to-guess(x ?x2) (y ?y)))
	(assert(to-guess(x ?x3) (y ?y)))
	(assert(to-guess (x (- ?x 1))(y ?y)))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
	(assert(to-guess (x ?x)(y (- ?y 1))))
	(assert(to-guess (x ?x)(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 2))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 2))(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 3))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 3))(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 4))(y ?y)))
	(assert(to-guess (x (+ ?x 4))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 4))(y (- ?y 1))))
	(assert(exec (step ?s) (action guess) (x ?x1) (y ?y)))
	(assert (ausiliar-exec (action guess) (x ?x2 ) (y ?y)))
    (assert (ausiliar-exec (action guess) (x ?x3 ) (y ?y)))
	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
	(printout t "(to-indecision-top-find-middle)la fire ha trovato un middle quindi abbiamo trovato una barca lunga 4 in " ?x " " ?x1 " " ?x2 " " ?x3 crlf)
	(retract ?f)
	(assert(return))
    (pop-focus)
)

; caso in cui ho trovato una nave di lunghezza 2, 2 caselle sotto "top" invece ci sarà "water"
(defrule to-indecision-top-find-wather (declare(salience 2))
    (status (step ?s)(currently running))
	?f <-(aux (x ?x)(y ?y)(content top))
	(not(k-cell (x ?x2&:(eq (+ ?x 2) ?x2))(y ?y)))
	?boats <- (closed-boats (two ?o))
	?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?row2 <- (k-per-row (row ?x1&:(eq (+ ?x 1) ?x1)) (num ?val-row2))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
	=>
	(modify ?boats(two (+ ?o 1)))
	(modify ?col1(num (- ?val-col1 2)))
	(modify ?row1(num (- ?val-row1 1)))
	(modify ?row2(num (- ?val-row2 1)))
	(assert(to-guess(x ?x) (y ?y)))
	(assert(to-guess(x ?x1) (y ?y)))
	(assert(to-guess (x (- ?x 1))(y ?y)))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
	(assert(to-guess (x ?x)(y (- ?y 1))))
	(assert(to-guess (x ?x)(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 2))(y ?y)))
	(assert(to-guess (x (+ ?x 2))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 2))(y (- ?y 1))))
	(assert(exec (step ?s) (action guess) (x ?x1) (y ?y)))
	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
	(printout t "(to-indecision-top-find-wather) la fire ha trovato un wather in x " (+ ?x 2) " y " ?y crlf)
	(retract ?f)
	(assert(return))
    (pop-focus)
)

; questa e la regola di indecisione per il bottom


(defrule indecision-bottom-one (declare (salience 1))
    (status (step ?s)(currently running))
	(moves (fires ?fires&:(> ?fires 0)))
	(k-cell (x ?x) (y ?y) (content bot))
    (test(>= (- ?x 2) 0))
	(not(guessed (x ?x)(y ?y)))
    (not(guessed (x ?x2 &:(eq (- ?x 2) ?x2))(y ?y)))
	(not(guessed (x ?x2 &:(eq (- ?x 2) ?x2))(y ?y)))
    ?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?row3 <- (k-per-row (row ?x2&:(eq (- ?x 2) ?x2)) (num ?val-row3))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
    (test (and (>= ?val-col1 3) (>= ?val-row3 1)))
    =>
    (assert (aux (x ?x)(y ?y)(content bot)))
	(assert(exec (step ?s) (action fire) (x (- ?x 2))(y ?y)))
	(printout t "(indecision-bottom-one) x "?x " y " ?y crlf)
	(assert(return))
    (pop-focus)
)

(defrule indecision-bottom-two 
    (status (step ?s)(currently running))
	(k-cell (x ?x) (y ?y) (content bot))
    (not(guessed (x ?x)(y ?y)))
    (closed-boats (two ?t0-val&~3))
    ?boats <- (closed-boats)
	?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?row2 <- (k-per-row (row ?x1&:(eq (- ?x 1) ?x1)) (num ?val-row2))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
    =>
    (modify ?boats(two (+ ?t0-val 1)))
	(modify ?col1(num (- ?val-col1 2)))
	(modify ?row1(num (- ?val-row1 1)))
	(modify ?row2(num (- ?val-row2 1)))
	(assert(to-guess (x ?x)(y ?y)))
	(assert(to-guess	(x ?x1)(y ?y)))
	(assert(to-guess (x (+ ?x 1))(y ?y)))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
	(assert(to-guess (x ?x)(y (+ ?y 1))))
	(assert(to-guess (x ?x)(y (- ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (- ?x 2))(y (- ?y 1))))
	(assert(to-guess (x (- ?x 2))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 2))(y ?y)))
	(assert(exec (step ?s) (action guess) (x (- ?x 1))(y ?y)))
	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
	(printout t " (indecision-bottom-two) guess in x " ?x1 " y " ?y crlf)
	(assert(return))
    (pop-focus)
)


(defrule to-indecision-bottom-find-middle  (declare (salience 2))
	(status (step ?s)(currently running))
	?f <-(aux (x ?x)(y ?y)(content bot))
	(k-cell (x ?x2 &:(eq (- ?x 2) ?x2))(y ?y)(content middle))
	?boats <- (closed-boats (four 0))
	?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?row2 <- (k-per-row (row ?x1&: (eq (- ?x 1) ?x1)) (num ?val-row2))
	?row3 <- (k-per-row (row ?x2) (num ?val-row3))
	?row4 <- (k-per-row (row ?x3&: (eq (- ?x 3) ?x3)) (num ?val-row4))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
	=>
	(modify ?boats(four 1))
	(modify ?col1(num (- ?val-col1 4)))
	(modify ?row1(num (- ?val-row1 1)))
	(modify ?row2(num (- ?val-row2 1)))
	(modify ?row3(num (- ?val-row3 1)))
	(modify ?row4(num (- ?val-row4 1)))
	(assert(to-guess(x ?x) (y ?y)))
	(assert(to-guess(x ?x1) (y ?y)))
	(assert(to-guess(x ?x2) (y ?y)))
	(assert(to-guess(x ?x3) (y ?y)))
	(assert(to-guess (x (+ ?x 1))(y ?y)))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
	(assert(to-guess (x ?x)(y (- ?y 1))))
	(assert(to-guess (x ?x)(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (- ?x 2))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 2))(y (- ?y 1))))
	(assert(to-guess (x (- ?x 3))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 3))(y (- ?y 1))))
	(assert(to-guess (x (- ?x 4))(y ?y)))
	(assert(to-guess (x (- ?x 4))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 4))(y (- ?y 1))))
	(assert(exec (step ?s) (action guess) (x ?x1) (y ?y)))
	(assert (ausiliar-exec (action guess) (x ?x2 ) (y ?y)))
    (assert (ausiliar-exec (action guess) (x ?x3 ) (y ?y)))
	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
	(printout t "(to-indecision-bottom-find-middle)la fire ha trovato un middle quindi abbiamo trovato una barca lunga 4 in " ?x " " ?x1 " " ?x2 " " ?x3 crlf)
	(retract ?f)
	(assert(return))
    (pop-focus)
)

(defrule to-indecision-bottom-find-wather (declare(salience 2))
    (status (step ?s)(currently running))
	?f <-(aux (x ?x)(y ?y)(content bot))
	(not(k-cell (x ?x2&:(eq (- ?x 2) ?x2))(y ?y)))
	?boats <- (closed-boats (two ?o))
	?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?row2 <- (k-per-row (row ?x1&:(eq (- ?x 1) ?x1)) (num ?val-row2))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
	=>
	(modify ?boats(two (+ ?o 1)))
	(modify ?col1(num (- ?val-col1 2)))
	(modify ?row1(num (- ?val-row1 1)))
	(modify ?row2(num (- ?val-row2 1)))
	(assert(to-guess(x ?x) (y ?y)))
	(assert(to-guess(x ?x1) (y ?y)))
	(assert(to-guess (x (+ ?x 1))(y ?y)))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
	(assert(to-guess (x ?x)(y (- ?y 1))))
	(assert(to-guess (x ?x)(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (- ?x 2))(y ?y)))
	(assert(to-guess (x (- ?x 2))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 2))(y (- ?y 1))))
	(assert(exec (step ?s) (action guess) (x ?x1) (y ?y)))
	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
	(printout t "(to-indecision-bottom-find-wather) la fire ha trovato un wather in x " (- ?x 2) " y  quindi concludo che la barca e lunga 2 " ?y crlf)
	(retract ?f)
	(assert(return))
    (pop-focus)
)

; questa e la regola di indecisione per il left


(defrule indecision-left-one (declare (salience 1))
    (status (step ?s)(currently running))
	(moves (fires ?fires&:(> ?fires 0)))
	(k-cell (x ?x) (y ?y) (content left))
    (test(< (+ ?y 2) 10))
	(not(guessed (x ?x)(y ?y)))
    (not(guessed (x ?x)(y ?y2 &:(eq (+ ?y 2) ?y2))))
	(not(guessed (x ?x)(y ?y2 &:(eq (+ ?y 2) ?y2))))
    ?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?col3 <- (k-per-col (col ?y2&:(eq (+ ?y 2) ?y2)) (num ?val-col3))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
    (test (and (>= ?val-col3 1) (>= ?val-row1 3)))
    =>
    (assert (aux (x ?x)(y ?y)(content left)))
	(assert(exec (step ?s) (action fire) (x ?x)(y (+ ?y 2))))
	(printout t "(indecision-left-one) x "?x " y " ?y crlf)
	(assert(return))
    (pop-focus)
)

(defrule indecision-left-two 
    (status (step ?s)(currently running))
	(k-cell (x ?x) (y ?y) (content left))
    (not(guessed (x ?x)(y ?y)))
    (closed-boats (two ?t0-val&~3))
    ?boats <- (closed-boats)
	?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?col2 <- (k-per-col (col ?y1&:(eq (+ ?y 1) ?y1)) (num ?val-col2))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
    =>
    (modify ?boats(two (+ ?t0-val 1)))
	(modify ?row1(num (- ?val-row1 2)))
	(modify ?col1(num (- ?val-col1 1)))
	(modify ?col2(num (- ?val-col2 1)))
	(assert(to-guess (x ?x)(y ?y)))
	(assert(to-guess	(x ?x)(y ?y1)))
	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (- ?x 1))(y ?y)))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 2))))
	(assert(to-guess (x ?x)(y (+ ?y 2))))
	(assert(to-guess (x ?x)(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y ?y)))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 2))))
	(assert(exec (step ?s) (action guess) (x ?x)(y ?y1)))
	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
	(printout t " (indecision-left-two) guess in x " ?x " y " ?y1 crlf)
	(assert(return))
    (pop-focus)
)


(defrule to-indecision-left-find-middle  (declare (salience 2))
	(status (step ?s)(currently running))
	?f <-(aux (x ?x)(y ?y)(content left))
	(k-cell (x ?x)(y ?y2 &:(eq (+ ?y 2) ?y2))(content middle))
	?boats <- (closed-boats (four 0))
	?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
	?col2 <- (k-per-col (col ?y1&: (eq (+ ?y 1) ?y1)) (num ?val-col2))
	?col3 <- (k-per-col (col ?y2) (num ?val-col3))
	?col4 <- (k-per-col (col ?y3&: (eq (+ ?y 3) ?y3)) (num ?val-col4))
	=>
	(modify ?boats(four 1))
	(modify ?row1(num (- ?val-row1 4)))
	(modify ?col1(num (- ?val-col1 1)))
	(modify ?col2(num (- ?val-col2 1)))
	(modify ?col3(num (- ?val-col3 1)))
	(modify ?col4(num (- ?val-col4 1)))
	(assert(to-guess(x ?x) (y ?y)))
	(assert(to-guess(x ?x) (y ?y1)))
	(assert(to-guess(x ?x) (y ?y2)))
	(assert(to-guess(x ?x) (y ?y3)))
	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (- ?x 1))(y ?y)))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 2))))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 3))))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 4))))
	(assert(to-guess (x ?x)(y (- ?y 1))))
	(assert(to-guess (x ?x)(y (+ ?y 4))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y ?y)))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 2))))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 3))))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 4))))
	(assert(exec (step ?s) (action guess) (x ?x) (y ?y1)))
	(assert (ausiliar-exec (action guess) (x ?x ) (y ?y2)))
    (assert (ausiliar-exec (action guess) (x ?x ) (y ?y3)))
	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
	(printout t "(to-indecision-top-find-middle)la fire ha trovato un middle quindi abbiamo trovato una barca lunga 4 in " ?y " " ?y1 " " ?y2 " " ?y3 crlf)
	(retract ?f)
	(assert(return))
    (pop-focus)
)

(defrule to-indecision-left-find-wather (declare(salience 2))
    (status (step ?s)(currently running))
	?f <-(aux (x ?x)(y ?y)(content left))
	(not(k-cell (x ?x)(y ?y2&:(eq (+ ?y 2) ?y2))))
	?boats <- (closed-boats (two ?o))
	?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
	?col2 <- (k-per-col (col ?y1&:(eq (+ ?y 1) ?y1)) (num ?val-col2))
	=>
	(modify ?boats(two (+ ?o 1)))
	(modify ?row1(num (- ?val-row1 2)))
	(modify ?col1(num (- ?val-col1 1)))
	(modify ?col2(num (- ?val-col2 1)))
	(assert(to-guess(x ?x) (y ?y)))
	(assert(to-guess(x ?x) (y ?y1)))
	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (- ?x 1))(y ?y)))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 2))))
	(assert(to-guess (x ?x)(y (- ?y 1))))
	(assert(to-guess (x ?x)(y (+ ?y 2))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y ?y)))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 2))))
	(assert(exec (step ?s) (action guess) (x ?x) (y ?y1)))
	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
	(printout t "(to-indecision-left-find-wather) la fire ha trovato un wather in x " ?x " y " (+ ?y 2) crlf)
	(retract ?f)
	(assert(return))
    (pop-focus)
)

; regole di indecisone per il right 


(defrule indecision-right-one (declare (salience 1))
    (status (step ?s)(currently running))
	(k-cell (x ?x) (y ?y) (content right))
    (test(>= (- ?y 2) 0))
	(moves (fires ?fires&:(> ?fires 0)))
	(not(guessed (x ?x)(y ?y)))
    (not(guessed (x ?x)(y ?y2 &:(eq (- ?y 2) ?y2))))
	(not(guessed (x ?x)(y ?y2 &:(eq (- ?y 2) ?y2))))
    ?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?col3 <- (k-per-col (col ?y2&:(eq (- ?y 2) ?y2)) (num ?val-col3))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
    (test (and (>= ?val-col3 1) (>= ?val-row1 3)))
    =>
    (assert (aux (x ?x)(y ?y)(content right)))
	(assert(exec (step ?s) (action fire) (x ?x)(y (- ?y 2))))
	(printout t "(indecision-right-one) x "?x " y " ?y crlf)
	(assert(return))
    (pop-focus)
)

(defrule indecision-right-two 
    (status (step ?s)(currently running))
	(k-cell (x ?x) (y ?y) (content right))
    (not(guessed (x ?x)(y ?y)))
    (closed-boats (two ?t0-val&~3))
    ?boats <- (closed-boats)
	?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?col2 <- (k-per-col (col ?y1&:(eq (- ?y 1) ?y1)) (num ?val-col2))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
    =>
    (modify ?boats(two (+ ?t0-val 1)))
	(modify ?row1(num (- ?val-row1 2)))
	(modify ?col1(num (- ?val-col1 1)))
	(modify ?col2(num (- ?val-col2 1)))
	(assert(to-guess (x ?x)(y ?y)))
	(assert(to-guess (x ?x)(y ?y1)))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y ?y)))
	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (- ?y 2))))
	(assert(to-guess (x ?x)(y (+ ?y 1))))
	(assert(to-guess (x ?x)(y (- ?y 2))))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y ?y)))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 2))))
	(assert(exec (step ?s) (action guess) (x ?x)(y ?y1)))
	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
	(printout t " (indecision-right-two) guess in x " ?x " y " ?y1 crlf)
	(assert(return))
    (pop-focus)
)


(defrule to-indecision-right-find-middle  (declare (salience 2))
	(status (step ?s)(currently running))
	?f <-(aux (x ?x)(y ?y)(content right))
	(k-cell (x ?x)(y ?y2 &:(eq (- ?y 2) ?y2))(content middle))
	?boats <- (closed-boats (four 0))
	?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
	?col2 <- (k-per-col (col ?y1&: (eq (- ?y 1) ?y1)) (num ?val-col2))
	?col3 <- (k-per-col (col ?y2) (num ?val-col3))
	?col4 <- (k-per-col (col ?y3&: (eq (- ?y 3) ?y3)) (num ?val-col4))
	=>
	(modify ?boats(four 1))
	(modify ?row1(num (- ?val-row1 4)))
	(modify ?col1(num (- ?val-col1 1)))
	(modify ?col2(num (- ?val-col2 1)))
	(modify ?col3(num (- ?val-col3 1)))
	(modify ?col4(num (- ?val-col4 1)))
	(assert(to-guess(x ?x) (y ?y)))
	(assert(to-guess(x ?x) (y ?y1)))
	(assert(to-guess(x ?x) (y ?y2)))
	(assert(to-guess(x ?x) (y ?y3)))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y ?y)))
	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (- ?y 2))))
	(assert(to-guess (x (- ?x 1))(y (- ?y 3))))
	(assert(to-guess (x (- ?x 1))(y (- ?y 4))))
	(assert(to-guess (x ?x)(y (- ?y 4))))
	(assert(to-guess (x ?x)(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y ?y)))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 2))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 3))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 4))))
	(assert(exec (step ?s) (action guess) (x ?x) (y ?y1)))
	(assert (ausiliar-exec (action guess) (x ?x ) (y ?y2)))
    (assert (ausiliar-exec (action guess) (x ?x ) (y ?y3)))
	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
	(printout t "(to-indecision-top-find-middle)la fire ha trovato un middle quindi abbiamo trovato una barca lunga 4 in " ?y " " ?y1 " " ?y2 " " ?y3 crlf)
	(retract ?f)
	(assert(return))
    (pop-focus)
)

(defrule to-indecision-right-find-wather (declare(salience 2))
    (status (step ?s)(currently running))
	?f <-(aux (x ?x)(y ?y)(content right))
	(not(k-cell (x ?x)(y ?y2&:(eq (- ?y 2) ?y2))))
	?boats <- (closed-boats (two ?o))
	?row1 <- (k-per-row (row ?x) (num ?val-row1))
	?col1 <- (k-per-col (col ?y) (num ?val-col1))
	?col2 <- (k-per-col (col ?y1&:(eq (- ?y 1) ?y1)) (num ?val-col2))
	=>
	(modify ?boats(two (+ ?o 1)))
	(modify ?row1(num (- ?val-row1 2)))
	(modify ?col1(num (- ?val-col1 1)))
	(modify ?col2(num (- ?val-col2 1)))
	(assert(to-guess(x ?x) (y ?y)))
	(assert(to-guess(x ?x) (y ?y1)))
	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (- ?x 1))(y ?y)))
	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (- ?x 1))(y (- ?y 2))))
	(assert(to-guess (x ?x)(y (- ?y 2))))
	(assert(to-guess (x ?x)(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y ?y)))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
	(assert(to-guess (x (+ ?x 1))(y (- ?y 2))))
	(assert(exec (step ?s) (action guess) (x ?x) (y ?y1)))
	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
	(printout t "(to-indecision-left-find-wather) la fire ha trovato un wather in x " ?x " y " (- ?y 2) crlf)
	(retract ?f)
	(assert(return))
    (pop-focus)
)










; (defrule to-indecision-top-find-bottom (declare(salience 2))
;     (status (step ?s)(currently running))
; 	?f <-(aux (x ?x)(y ?y)(content top))
; 	;questo componente non poteva esserci sicuramente prima perche deve esserci dell acqua per far attivare questa regola 
; 	(k-cell (x ?x2&:(eq (+ ?x 2) ?x2))(y ?y)(content bot))
; 	(not(guessed (x ?x2)(y ?y)))
; 	?boats <- (closed-boats (three ?o))
; 	?row1 <- (k-per-row (row ?x) (num ?val-row1))
; 	?row2 <- (k-per-row (row ?x1&:(eq (+ ?x 1) ?x1)) (num ?val-row2))
; 	?row3 <- (k-per-row (row ?x2) (num ?val-row3))
; 	?col1 <- (k-per-col (col ?y) (num ?val-col1))
; 	=>
; 	(modify ?boats(three (+ ?o 1)))
; 	(modify ?col1(num (- ?val-col1 3)))
; 	(modify ?row1(num (- ?val-row1 1)))
; 	(modify ?row2(num (- ?val-row2 1)))
; 	(modify ?row3(num (- ?val-row3 1)))
; 	(assert(to-guess(x ?x) (y ?y)))
; 	(assert(to-guess(x ?x1) (y ?y)))
; 	(assert(to-guess(x ?x2) (y ?y)))
; 	(assert(to-guess (x (- ?x 1))(y ?y)))
; 	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
; 	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
; 	(assert(to-guess (x ?x)(y (- ?y 1))))
; 	(assert(to-guess (x ?x)(y (+ ?y 1))))
; 	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
; 	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
; 	(assert(to-guess (x (+ ?x 2))(y (+ ?y 1))))
; 	(assert(to-guess (x (+ ?x 2))(y (- ?y 1))))
; 	(assert(to-guess (x (+ ?x 3))(y ?y)))
; 	(assert(to-guess (x (+ ?x 3))(y (+ ?y 1))))
; 	(assert(to-guess (x (+ ?x 3))(y (- ?y 1))))
; 	(assert(exec (step ?s) (action guess) (x ?x1) (y ?y)))
; 	(assert (ausiliar-exec (action guess) (x ?x2 ) (y ?y)))
; 	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
; 	(printout t "(to-indecision-top-find-bottom) la fire ha trovato un bottom quindi abbiamo trovato una barca lunga 3 in " ?x " " ?x1 " " ?x2 crlf)
; 	(retract ?f)
; 	(assert(return))
;     (pop-focus)
; )


; (defrule to-indecision-bottom-find-top (declare(salience 2))
;     (status (step ?s)(currently running))
; 	?f <-(aux (x ?x)(y ?y)(content bot))
; 	(k-cell (x ?x2&:(eq (- ?x 2) ?x2))(y ?y)(content top))
; 	(not(guessed (x ?x2)(y ?y)))
; 	?boats <- (closed-boats (three ?o))
; 	?row1 <- (k-per-row (row ?x) (num ?val-row1))
; 	?row2 <- (k-per-row (row ?x1&:(eq (- ?x 1) ?x1)) (num ?val-row2))
; 	?row3 <- (k-per-row (row ?x2) (num ?val-row3))
; 	?col1 <- (k-per-col (col ?y) (num ?val-col1))
; 	=>
; 	(modify ?boats(three (+ ?o 1)))
; 	(modify ?col1(num (- ?val-col1 3)))
; 	(modify ?row1(num (- ?val-row1 1)))
; 	(modify ?row2(num (- ?val-row2 1)))
; 	(modify ?row3(num (- ?val-row3 1)))
; 	(assert(to-guess(x ?x) (y ?y)))
; 	(assert(to-guess(x ?x1) (y ?y)))
; 	(assert(to-guess(x ?x2) (y ?y)))
; 	(assert(to-guess (x (+ ?x 1))(y ?y)))
; 	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
; 	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
; 	(assert(to-guess (x ?x)(y (- ?y 1))))
; 	(assert(to-guess (x ?x)(y (+ ?y 1))))
; 	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
; 	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
; 	(assert(to-guess (x (- ?x 2))(y (+ ?y 1))))
; 	(assert(to-guess (x (- ?x 2))(y (- ?y 1))))
; 	(assert(to-guess (x (- ?x 3))(y ?y)))
; 	(assert(to-guess (x (- ?x 3))(y (+ ?y 1))))
; 	(assert(to-guess (x (- ?x 3))(y (- ?y 1))))
; 	(assert(exec (step ?s) (action guess) (x ?x1) (y ?y)))
; 	(assert (ausiliar-exec (action guess) (x ?x2 ) (y ?y)))
; 	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
; 	(printout t "(to-indecision-bottom-find-top) la fire ha trovato un top quindi abbiamo trovato una barca lunga 3 in " ?x " " ?x1 " " ?x2 crlf)
; 	(retract ?f)
; 	(assert(return))
;     (pop-focus)
; )


; (defrule to-indecision-left-find-right (declare(salience 2))
;     (status (step ?s)(currently running))
; 	?f <-(aux (x ?x)(y ?y)(content left))
; 	(k-cell (x ?x)(y ?y2&:(eq (+ ?y 2) ?y2))(content right))
; 	(not(guessed (x ?x)(y ?y2)))
; 	?boats <- (closed-boats (three ?o))
; 	?row1 <- (k-per-row (row ?x) (num ?val-row1))
; 	?col1 <- (k-per-col (col ?y) (num ?val-col1))
; 	?col2 <- (k-per-col (col ?y1&:(eq (+ ?y 1) ?y1)) (num ?val-col2))
; 	?col3 <- (k-per-col (col ?y2) (num ?val-col3))
; 	=>
; 	(modify ?boats(three (+ ?o 1)))
; 	(modify ?row1(num (- ?val-row1 3)))
; 	(modify ?col1(num (- ?val-col1 1)))
; 	(modify ?col2(num (- ?val-col2 1)))
; 	(modify ?col3(num (- ?val-col3 1)))
; 	(assert(to-guess(x ?x) (y ?y)))
; 	(assert(to-guess(x ?x) (y ?y1)))
; 	(assert(to-guess(x ?x) (y ?y2)))
; 	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
; 	(assert(to-guess (x (- ?x 1))(y ?y)))
; 	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
; 	(assert(to-guess (x (- ?x 1))(y (+ ?y 2))))
; 	(assert(to-guess (x (- ?x 1))(y (+ ?y 3))))
; 	(assert(to-guess (x ?x)(y (- ?y 1))))
; 	(assert(to-guess (x ?x)(y (+ ?y 3))))
; 	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
; 	(assert(to-guess (x (+ ?x 1))(y ?y)))
; 	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
; 	(assert(to-guess (x (+ ?x 1))(y (+ ?y 2))))
; 	(assert(to-guess (x (+ ?x 1))(y (+ ?y 3))))
; 	(assert(exec (step ?s) (action guess) (x ?x) (y ?y1)))
; 	(assert (ausiliar-exec (action guess) (x ?x ) (y ?y2)))
; 	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
; 	(printout t "(to-indecision-left-find-right) la fire ha trovato un bottom quindi abbiamo trovato una barca lunga 3 in " ?y " " ?y1 " " ?y2 crlf)
; 	(retract ?f)
; 	(assert(return))
;     (pop-focus)
; )



; (defrule to-indecision-right-find-left (declare(salience 2))
;     (status (step ?s)(currently running))
; 	?f <-(aux (x ?x)(y ?y)(content right))
; 	(k-cell (x ?x)(y ?y2&:(eq (- ?y 2) ?y2))(content left))
; 	(not(guessed (x ?x)(y ?y2)))
; 	?boats <- (closed-boats (three ?o))
; 	?row1 <- (k-per-row (row ?x) (num ?val-row1))
; 	?col1 <- (k-per-col (col ?y) (num ?val-col1))
; 	?col2 <- (k-per-col (col ?y1&:(eq (- ?y 1) ?y1)) (num ?val-col2))
; 	?col3 <- (k-per-col (col ?y2) (num ?val-col3))
; 	=>
; 	(modify ?boats(three (+ ?o 1)))
; 	(modify ?row1(num (- ?val-row1 3)))
; 	(modify ?col1(num (- ?val-col1 1)))
; 	(modify ?col2(num (- ?val-col2 1)))
; 	(modify ?col3(num (- ?val-col3 1)))
; 	(assert(to-guess(x ?x) (y ?y)))
; 	(assert(to-guess(x ?x) (y ?y1)))
; 	(assert(to-guess(x ?x) (y ?y2)))
; 	(assert(to-guess (x (- ?x 1))(y (+ ?y 1))))
; 	(assert(to-guess (x (- ?x 1))(y ?y)))
; 	(assert(to-guess (x (- ?x 1))(y (- ?y 1))))
; 	(assert(to-guess (x (- ?x 1))(y (- ?y 2))))
; 	(assert(to-guess (x (- ?x 1))(y (- ?y 3))))
; 	(assert(to-guess (x ?x)(y (- ?y 3))))
; 	(assert(to-guess (x ?x)(y (+ ?y 1))))
; 	(assert(to-guess (x (+ ?x 1))(y (+ ?y 1))))
; 	(assert(to-guess (x (+ ?x 1))(y ?y)))
; 	(assert(to-guess (x (+ ?x 1))(y (- ?y 1))))
; 	(assert(to-guess (x (+ ?x 1))(y (- ?y 2))))
; 	(assert(to-guess (x (+ ?x 1))(y (- ?y 3))))
; 	(assert(exec (step ?s) (action guess) (x ?x) (y ?y1)))
; 	(assert (ausiliar-exec (action guess) (x ?x ) (y ?y2)))
; 	(assert (ausiliar-exec (action guess) (x ?x)(y ?y)))
; 	(printout t "(to-indecision-right-find-right) la fire ha trovato un left quindi abbiamo trovato una barca lunga 3 in " ?y " " ?y1 " " ?y2 crlf)
; 	(retract ?f)
; 	(assert(return))
;     (pop-focus)
; )