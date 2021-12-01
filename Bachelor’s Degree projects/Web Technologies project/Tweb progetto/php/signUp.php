<?php
session_start();
    
    if(isset($_POST['submitSign'])){
        include_once "dbServer.php";
        //recuper i dati dell'utente
        $name = htmlspecialchars($_POST['name']);
        $surname = htmlspecialchars($_POST['surname']);
        $email = htmlspecialchars($_POST['email']);
        $psw = htmlspecialchars($_POST['psw']);
        $confPsw = htmlspecialchars($_POST['confPsw']);

        //Controllo se l'input per nome e cognome sono validi
        if(!preg_match("/^[a-zA-Z]*$/",$name) || !preg_match("/^[a-zA-Z]*$/",$surname)){
            echo "Errore nome o cognome, sono accettati solo caratteri";
            header("Location: ../index.php?signUp=invalid name or username");
            exit();
        }else{
            //Controllo se l'email è valida e non è stata già usata
            $sql = "SELECT user_email FROM users WHERE user_email = '$email';";
            $result=$conn->prepare($sql);
            $result->execute();
            if(!filter_var($email,FILTER_VALIDATE_EMAIL) || ($result->rowCount()>0)){
                echo "Errore formato email non valido.";
                header("Location: ../index.php?signUp=invalidEmail");
                exit();
            }else{
                if($psw !== $confPsw){
                    echo "Errore password non uguali.";
                    header("Location: ../index.php?signUp=passNot match");
                    exit();
                }else{
                    //Hashing la password, in modo da non renderla visibile nel database.
                    $hashPsw =  password_hash($psw,PASSWORD_DEFAULT);
                    //Inserisco l'utente nel database
                    $sql = "INSERT INTO users (user_first, user_last, user_email, user_psw) VALUES ('$name', '$surname', '$email', '$hashPsw');";
                    $result=$conn->prepare($sql);
                    $result->execute();
                    $_SESSION['u_first'] = $name;
                    $_SESSION['u_last'] = $username;
                    $_SESSION['u_email'] = $email;
                    $_SESSION['u_psw'] = $psw;
                    header("Location: ../HomePage/index.php?signUp=success");
                    exit();
                }
            }
        }
    }else{
        echo "Errore il form di registrazione non è settato";
        header("Location: ../index.php?signUp=formUnset");
        exit();
    }
?>
  

    