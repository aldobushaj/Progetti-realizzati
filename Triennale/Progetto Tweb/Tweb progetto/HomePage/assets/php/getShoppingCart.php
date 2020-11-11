<?php
/* Questo file php gestisce tutte le possibili operazioni applicabili al carrello(aggiunta di un articolo,rimozione o acquisto)*/
include_once "../../../php/dbServer.php"; 
//Devo aver per forza aver ricevuto l'email, altrimenti notifico(tramite query string) l'errore
if(isset($_POST['email']) && isset($_POST['watch'])){
    //recupero l'orologio
    $watch = htmlspecialchars($_POST['watch']);
    $watch = $conn->quote($watch);
    //recupero l'email
    $email = htmlspecialchars($_POST['email']);
    $email = $conn->quote($email);

    // query per verificare se l'orologio è gia presente nel database
    $getItem = "SELECT * FROM shoppingcart_list WHERE user_email = $email AND shopping_cart = $watch;";

    //Nel primo caso gestisco l'inserimento nel carrello di un orologio
    if(isset($_POST['add']) && $_POST['add'] == 1){

            //se non è gia presente lo inserisco mediante la seguente query
            $insertShoppingCart = "INSERT INTO shoppingcart_list (user_email, shopping_cart,number_items) VALUES ($email, $watch,1);";
            
            try { 
                //Mi collego al database e verifico se l'articolo è gia presente nel carrello
                $stat=$conn->prepare($getItem);
                $stat->execute();
                //$stat = $conn->query($getItem);  

                //Se row è settato c'è almeno un orologio quindi....
                if($row = $stat-> fetch(PDO::FETCH_ASSOC)){
                    // aggiorno il numero di articoli nel carrello
                    $number = $row['number_items']+1;
                    $addShoppingCart = "UPDATE shoppingcart_list SET number_items = '$number' WHERE user_email = $email AND shopping_cart = $watch;";
                    // e quindi aggiorno il numero di elementi nel database
                    $stat=$conn->prepare($addShoppingCart);
                    $stat->execute();
                    //$conn->query($addShoppingCart);
                    // restituisco il numero di articoli dell'orologio specificato
                    echo $number;
                }else{
                    // Se entro qui questo è il primo articolo aggiunto al carrello, quindi il numero di articoli(di questo orologio) sarà 1
                    $stat=$conn->prepare($insertShoppingCart);
                    $stat->execute();
                    //$conn->query($insertShoppingCart);
                    // restituisco il numero di articoli
                    echo 1;
                }
                
            // gestisco l'errore notificandolo con un messagio d'errore
            }catch(Exception $e) {
                echo 'Connection failed: ' . $e->getMessage();
            }

    }else{// Mentre in questo caso gestisco la rimozione dell'articolo specificato

        //Query che elimina il prodotto specificato
        $dropItem = "DELETE FROM shoppingcart_list WHERE shopping_cart = $watch AND user_email = $email;";
        
        //verifico che ci sia un orologio
        $stat=$conn->prepare($getItem);
        $stat->execute();
        //$stat = $conn->query($getItem);  
        $items = $stat-> fetch(PDO::FETCH_ASSOC);

        try {
            if($items['number_items'] > 1){
                //recupero il numero di orologio e ne sottrago uno perchè è stato cliccato il pulsante che elimina un articolo
                $prova = $items['number_items']-1;
                $prova = $conn->quote($prova);
                
                $deleteOneItem = "UPDATE shoppingcart_list SET number_items = $prova WHERE user_email = $email AND shopping_cart = $watch;";
                //elimino un orologio dal carrello
                $stat=$conn->prepare($deleteOneItem);
                $stat->execute();
                //$stat = $conn->query($deleteOneItem);
            }else {

                //Mi collego al database ed elimino l'articolo
                $stat=$conn->prepare($dropItem);
                $stat->execute();
                //$stat = $conn->query($dropItem);
            }

        //gestisco l'errore notificandolo con un messagio d'errore
        }catch(Exception $e) {
            echo 'Connection failed: ' . $e->getMessage();
        }
    }
// L'ultimo caso restituisce tutti gli articoli presenti nel carrello di un dato utente
}else if(isset($_POST['email'])){

    //recupero l'email
    $email = htmlspecialchars($_POST['email']); 
    $email = $conn->quote($email);      
    
    //sono stati acquistati gli articoli nel carrello quindi li rimuovo
    if(isset($_POST['add']) && $_POST['add'] == 0){
       
        //elimino tutti gli articoli nel carrello in quanto sono stati acquistati
        $checkout = "DELETE FROM shoppingcart_list WHERE user_email = $email;";
            
        try {
            //Mi collego al database ed elimino gli articoli acquistati
            $stat=$conn->prepare($checkout);
            $stat->execute();
            //$stat = $conn->query($checkout);
        // gestisco l'errore notificandolo con un messagio d'errore
        }catch(Exception $e) {
            echo 'Connection failed: ' . $e->getMessage();
        }
    }else{
        //oggetto contenete dati da ritornare alla chiamata AJAX 
        $jsonOb = [];  

        //Recupero tutti i prodotti che l'utente specificato dall'email ricevuta (dalla chiamata ajax) ha nel carrello
        $getShoppingCart = "SELECT shoppingcart_list.shopping_cart ,items.price, shoppingcart_list.number_items FROM shoppingcart_list INNER JOIN items ON shoppingcart_list.shopping_cart = name WHERE user_email = $email;";
        
        try {
            //Mi collego al database per avere le informazioni
            $stat=$conn->prepare($getShoppingCart);
            $stat->execute();
            //$stat = $conn->query($getShoppingCart);

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