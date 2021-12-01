<?php
//Questo file gestisce tutte le operazioni possibili alla lista preferiti(caricamento e inserimento aritcoli)
//Devo aver per forza aver ricevuto l'email, altrimenti notifico(tramite query string) l'errore
if(isset($_POST['email'])){
    include_once "../../../php/dbServer.php";   

    $jsonOb = [];               //oggetto contenete dati da ritornare alla chiamata AJAX
    $email = htmlspecialchars($_POST['email']);  //recupero l'email 
    $email = $conn->quote($email);

    //sono nel caso in cui è stato cliccato il bottone "elimina" o "add" quindi mi comporto opportunamente
    if(isset($_POST['watch']) && isset($_POST['add'])){ 
        //recupero il nome dell'orologio
        $watch = htmlspecialchars($_POST['watch']);
        $watch = $conn->quote($watch);

        // Il dato ricevuto indica se è stato cliccato il bottone "add" (0 = false)
        if(($_POST['add']) == 1 ){
            // Query per verificare se l'orologio è gia presente nel database
            $getName = "SELECT * FROM favorite_list WHERE user_email = $email AND favorite = $watch;";

            // Se non è gia presente lo inserisco
            $insertFavorite = "INSERT INTO favorite_list (user_email, favorite) VALUES ($email, $watch);";

            try {
                //Mi collego al database e verifico che non sia già presente
                $stat=$conn->prepare($getName);
                $stat->execute();
                //Se num row è maggiore di 1 vuol dire che l'orologio che si vuole aggiungere è gia presente nel database
                //quindi restituisco una stringa usata opportunamente dal chiamante
                if(($stat->rowCount()) > 0){            
                    echo "Articolo già aggiunto ai preferiti";
                }else{
                    //altrimenti lo inserisco
                    $stat=$conn->prepare($insertFavorite);
                    $stat->execute();
                }
    
            // gestisco l'errore notificandolo con un messagio d'errore
            }catch(Exception $e) {
                echo 'Connection failed: ' . $e->getMessage();
            }

        }else{//add è uguale a false, significa che è stato cliccato il bottone "elimina", quindi elimino l'articolo
            // Query che elimina il prodotto specificato
            $dropFavorite = "DELETE FROM favorite_list WHERE favorite = $watch AND user_email = $email;";

            try {
                //Mi collego al database ed elimino l'articolo
                $stat=$conn->prepare($dropFavorite);
                $stat->execute();
    
            // gestisco l'errore notificandolo con un messagio d'errore
            }catch(Exception $e) {
                echo 'Connection failed: ' . $e->getMessage();
            }
        } 

    }else{//se ho ricevuto solo l'email devo semplicemente caricare la lista dei preferiti

        //Recupero tutti i prodotti che l'utente specificato dall'email ricevuta (dalla chiamata AJAX) ha nei preferiti
        $getFavorite = "SELECT favorite_list.favorite ,items.price FROM favorite_list INNER JOIN items ON favorite_list.favorite = name WHERE user_email = $email;";
    
        try {
            //Mi collego al database per avere le informazioni
            $stat=$conn->prepare($getFavorite);
            $stat->execute();

            while($ris = $stat-> fetch(PDO::FETCH_ASSOC)){
                $jsonOb[] = $ris;       //inserisco i dati nell'oggetto json
            }
            echo json_encode($jsonOb);  //restituisco l'oggetto JSON

        // gestisco l'errore notificandolo con un messagio d'errore
        }catch(Exception $e) {
            echo 'Connection failed: ' . $e->getMessage();
        }

    }

}else{//condizione d'errore
    header("Location: ../../index.php?error_Unset_Email_Favorite");
    exit();
}     