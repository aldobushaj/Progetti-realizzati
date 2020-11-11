<?php
session_start();
    
    if(isset($_POST['submitLog'])){
        include_once "dbServer.php";
        //recupero la password
        $psw = htmlspecialchars($_POST['psw']);
        
        //recupero l'email
        $email = htmlspecialchars($_POST['email']);
        $email = $conn->quote($email);

        //recupero gli utenti con l'email specificata
        $sql = "SELECT * FROM users WHERE user_email = $email";
        $result=$conn->prepare($sql);
        $result->execute();
        
        //se l'utente non è registrato ritorno al login
        if(($result->rowCount()) < 1){
            header("Location: ../index.php?login=error");
            exit();
        }else{//altrimenti...
            if($row = $result-> fetch(PDO::FETCH_ASSOC)){
                //De-hashing della password
                $hashPswCheck = password_verify($psw,$row['user_psw']);
                //se la password nel database e quella inserita non corrispondono notifico l'errore
                if($hashPswCheck == false){
                    header("Location: ../index.php?password=error");
                    exit();
                }else if($hashPswCheck == true){
                    //Eseguo il Log-in dell'user
                    $_SESSION['u_first'] = $row['user_first'];
                    $_SESSION['u_last'] = $row['user_last'];
                    $_SESSION['u_email'] = $row['user_email'];
                    $_SESSION['u_psw'] = $row['user_psw'];
                    header("Location: ../HomePage/index.php?login=success");
                    exit();
                    
                } 
            }
        }

    }else{
        echo "Errore il form login non è settato";
        header("Location: ../index.php?login=formUnset");
        exit();
    }