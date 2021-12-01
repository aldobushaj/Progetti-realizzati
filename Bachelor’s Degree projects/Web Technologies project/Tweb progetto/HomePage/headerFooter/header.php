<!--Header contenente bottoni login, logout , home e imagine logo -->
<?php 
    session_start();
    if((!isset($_SESSION['u_email']))){
        header("Location: ../index.php?login=notLoggedIn");
        exit();
    }
    
 ?>
<!DOCTYPE html>
<html>
<head>

  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1">
  

  <link rel="stylesheet" href="assets/css/default/mobirise-icons.css">
  <link rel="stylesheet" href="assets/css/default/bootstrap.min.css">
  <link rel="stylesheet" href="assets/css/default/animate.min.css">
  <link rel="stylesheet" href="assets/css/default/mbr-additional.css" type="text/css">
  <link rel="stylesheet" href="assets/css/default/style.css">
  <link rel="stylesheet" href="assets/css/default/styleHome.css">
  <link rel="stylesheet" href="assets/css/style.css">

  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
  <script src="assets/js/navbar-dropdown.js"></script>    <!--  Classe animazione scorrimento pagine-->
  <script src="assets/js/loadFavorite.js"></script>       <!--  Classe per il caricamento e le operazioni sulla lista preferiti-->
  <script src="assets/js/loadReviews.js"></script>        <!--  Classe per il caricamento e le operazioni sulle recensioni-->
  <script src="assets/js/loadShoppingCart.js"></script>   <!--  Classe per il caricamento e le operazioni sul carrello-->
  
</head>

<body>
    <section class="menu cid-ryz58Y1Gsf" once="menu" id="menu1-k ">
        <nav class="navbar navbar-expand beta-menu navbar-dropdown align-items-center navbar-fixed-top navbar-toggleable-sm bg-color transparent">
            
            <input type="hidden" id="email" value="<?= $_SESSION['u_email']?>">
            <div class="menu-logo">
                 <!-- Serve ad accedere ai valori della sessione su JS -->
                <input type="hidden" id="u_email" value="<?= $_SESSION['u_email']?>">
                <div class="navbar-brand">
                    <a href="index.php">
                        <span class="navbar-logo">
                            <img src="assets/images/mbr-192x128.jpg" alt="Mobirise" title="" style="height: 4.9rem;"> 
                        </span>
                    </a>  
                    <span class="navbar-caption text-white display-5">
                        AB Watches
                    </span>
                    
                    <span class="pl-5 mbr-fonts-style display-10 text-secondary">
                        <strong> Welcome <?= $_SESSION['u_email']; ?> </strong>
                    </span>
                </div>
            </div>

            <div class="navbar-buttons mbr-section-btn">  
                <form  action="../php/logOut.php" method="POST">
                    <a class="btn btn-sm  btn-primary-outline display-4" href="index.php">
                        <span class="mbri-home mbr-iconfont mbr-iconfont-btn"></span>
                        <span style="font-size: 25.6px;"><br></span>&nbsp;Home<br>
                    </a> 

                    <a class="btn btn-sm btn-primary-outline display-4" href="preferiti.php">
                        <span class="mbri-hearth mbr-iconfont mbr-iconfont-btn"></span>
                    </a> 
                    
                    <a class="btn btn-sm btn-primary-outline display-4" href="carrello.php">
                        <span class="mbri-shopping-cart mbr-iconfont mbr-iconfont-btn"></span>
                        <font face="MobiriseIcons"></font>
                        <span style="font-size: 25.6px;">Carrello</span><br>
                    </a>

                    <button class="btn btn-sm btn-white text-black display-4" type="submit" name="logOut">
                        <span class="mbri-logout mbr-iconfont mbr-iconfont-btn "></span> 
                    Logout</button>
                </form>
            </div>
        </nav>
    </section>