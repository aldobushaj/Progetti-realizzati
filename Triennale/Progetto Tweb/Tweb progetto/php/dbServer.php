<?php
           
    $servername = "localhost";
    $username = "root";
    $password = "password";

    try {
        $conn = new PDO("mysql:host=$servername;dbname=tweb", $username, $password);
        // set the PDO error mode to exception
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);    //,PDO::ATTR_EMULATE_PREPARES=false      da mettere per eveitare attacchi mysql injection
        $conn->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);//evito attacchi mysql injection
        
        }
    catch(PDOException $e)
        {
        echo "Connection failed: " . $e->getMessage();
        }
