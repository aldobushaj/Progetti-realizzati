from mazelib import Maze
from mazelib.generate.Prims import Prims

def line_prepender(filename, line):
    with open(filename, 'r+') as f:
        content = f.read()
        f.seek(0, 0)
        f.write(line.rstrip('\r\n') + '\n' + content)

m = Maze()
m.generator = Prims(10, 10) #dimensioni labirinto
m.generate()

#solve maze
from mazelib.solve.BacktrackingSolver import BacktrackingSolver
m.solver = BacktrackingSolver()
m.generate_entrances()
m.solve()


print(m)
f = open("labirinto.txt", "w")
f.write(str(m))
f.close()

colonna=1
riga=1
l = open("labirinto1.pl", "w")

num_colonne=0

with open("labirinto.txt") as fileobj:

    for line in fileobj:
          
        for ch in line: 
            if ( ch is '#'):
               l.write("occupata(pos("+str(riga)+","+str(colonna)+")).\n")
            if ( ch is 'S'):
                #l.write("iniziale(pos("+str(riga)+","+str(colonna)+")).\n")
                iniziale="iniziale(pos("+str(riga)+","+str(colonna)+")).\n"
            if ( ch is 'E'):
                #l.write("finale(pos("+str(riga)+","+str(colonna)+")).\n")
                finale="finale(pos("+str(riga)+","+str(colonna)+")).\n"
            colonna+=1
        num_colonne=colonna-1
        colonna=1
        riga+=1
    l.close()
    #l.write("num_righe("+str(riga-1)+").\n")
    #l.write("num_colonne("+str(num_colonne)+").\n")
    line_prepender("labirinto1.pl",finale)
    line_prepender("labirinto1.pl",iniziale)
    line_prepender("labirinto1.pl","num_colonne("+str(num_colonne)+").\n")
    line_prepender("labirinto1.pl","num_righe("+str(riga-1)+").\n" )
    


l.close()



