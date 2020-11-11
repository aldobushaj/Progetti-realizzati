<?php

    if(isset($_POST['logOut'])){
        session_start();
        session_unset();
        session_destroy();
        header("Location: ../index.php");
        exit();

    }else{
        echo "la sessione non è settata";
    }


?>