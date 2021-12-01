<?php
//Classe in cui devo caricare i commenti o inserirne uno
//Devo aver per forza aver ricevuto l'email, altrimenti notifico(tramite query string) l'errore
if(isset($_POST['email']) && isset($_POST['watch'])){ 
    include_once "../../../php/dbServer.php";   
    //oggetto contenete dati da ritornare alla chiamata AJAX
    $jsonOb = [];               

    //recupero il nome dell'orologio
    $watch = htmlspecialchars($_POST['watch']);       
    $watch = $conn->quote($watch);

    //recupero l'email
    $email = htmlspecialchars($_POST['email']);      
    $email = $conn->quote($email);
    
    //Gestisco il caso dell'inserimento di un commento
    if(isset($_POST['review'])){  

        //recupero la recensione
        $review = htmlspecialchars($_POST['review']);
        $review = $conn->quote($review);        

        //Creo l'oggetto che conterra tutte le informazioni utili da ritornare alla chiamata AJAX
        $jsonOb = (object) [
            "item"=> $watch,            //nome dell'orologio
            "user_email"=> $email,     //email dell'utente
            "review"=> $review        //recensione
        ];

        try {
            //Query che agginuge la recensione appena aggiunta dall'utente
            $addReview = "INSERT INTO reviews (item, user_email, review) VALUES ($jsonOb->item,$jsonOb->user_email, $jsonOb->review);";
            
            //inserisco nel database, non c'è bisogno di restituire niente alla chiamata AJAX
            $stat=$conn->prepare($addReview);
            $stat->execute();
            

        // gestisco l'errore notificandolo con un messagio d'errore            
        }  catch(Exception $e) {
            echo 'Connection failed: ' . $e->getMessage();
        }
        
    //Gestisco il caso del caricamento dei commenti  
    }else{ 
    
        //Query che ritorna come risultato tutte le recensioni di un specifico utente e di uno specifico prodotto
        $all_reviews = "SELECT user_email,review FROM reviews WHERE item = $watch;";

        try {
            //Mi collego al database per avere le informazioni,con l'opportuna query
            $stat=$conn->prepare($all_reviews);
            $stat->execute();
    
        // gestisco l'errore notificandolo con un messagio d'errore
        }catch(Exception $e) {
            echo 'Connection failed: ' . $e->getMessage();
        }
    
        while($ris = $stat-> fetch(PDO::FETCH_ASSOC)){
            $jsonOb[] = $ris;       //inserisco i dati nell'oggetto json
        }
        echo json_encode($jsonOb);  //restituisco l'oggetto JSON
            

    }//Fine caso caricamento delle recensioni

}else{//condizione d'errore
    header("Location: ../../index.php?error_Unset_Email_Reviews");
    exit();
}  