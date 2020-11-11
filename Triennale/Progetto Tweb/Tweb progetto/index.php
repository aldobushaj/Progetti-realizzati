<!--Bushaj Aldo matricola: 847091-->
<!--Pagina iniziale in cui è possibile solamente fare il login o registrare un nuovo utente-->
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1">

  <title>Home Page</title>

  <link rel="stylesheet" href="css/cssForm.css">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Playfair+Display:400,700&subset=latin,cyrillic">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Alegreya+Sans:400,700&subset=latin,vietnamese,latin-ext">
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <link rel="stylesheet" href="css/style.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="js/insertData.js"></script>

  
</head>
<body>
  <section class="mbr-section mbr-section-full mbr-parallax-background" id="header4-0" data-rv-view="0" style="background-image: url(HomePage/assets/images/mbr-1920x1280.jpg);">
    <div class="mbr-table-cell">
        <div class="mbr-overlay" ></div>
        <div class="container">
            <div class="row">
                <div class="col-md-8 col-md-offset-4 text-xs-right">

                    <h1 class="mbr-section-title display-1">AB Watches</h1>
                    <p class="lead">Lasciati incantare dalla collezione di orologi di lusso AB Watches</p>
                    <div class="mbr-buttons-left">
                        <button id="log" class="btn btn-lg btn-warning " style="width:auto;">Login</button> 
                        <a id="sign" class="btn btn-lg btn-warning" style="width:auto;">Sign Up</a> </div>
                </div>
            </div>
        </div>
        
    </div>

    
    <div id="login" class="modal">
  
        <form class="modal-content animate" action="php/login.php" method="POST">
            <div class="alert alert-success" role="alert">
              Autentication  <span class="close" title="Close">&times;</span>
            </div>
          
      
          <div class="container">
            <label for="uname"><b>Email</b></label>
            <input type="text" placeholder="Enter Email" name="email" required>
      
            <label for="psw"><b>Password</b></label>
            <input id="pswLog" type="password" placeholder="Enter Password" name="psw" required>
              
            <button name= "submitLog" type="submit">Login</button>
            
          </div>
      
          <div class="container">
            <button id="cancel" type="button" class="cancelbtn">Cancel</button>
          </div>

        </form>
    </div>

    <div id="signUp" class="modal">
  
            <form class="modal-content animate" action="php/signUp.php" method="POST" >
                <div class="alert alert-success" role="alert">
                     Registration <span class="close" title="Close">&times;</span>
                </div> 
          
              <div class="container">
                
                <label for="uname"><b>Name</b></label>
                <input id="reg-name" type="text" placeholder="Enter name" name="name" required>

                <label for="uname"><b>Surname</b></label>
                <input id="reg-surname" type="text" placeholder="Enter Surname" name="surname" required>

                <label for="uname"><b>Email</b></label>
                <input id="reg-email" type="text" placeholder="Enter Email" name="email" required>
          
                <label for="psw"><b>Password</b></label>
                <input id="reg-psw" type="password" placeholder="Enter Password" name="psw" required >
                
                <label for="psw"><b>Confirm Password</b></label>
                <input id="reg-confpsw" type="password" placeholder="Confirm Password" name="confPsw" required >
                      
                <button name = "submitSign" type="submit">Sign Up</button>
                <p class="reg-message"></p>

              </div>
          
            
            </form>
    </div>
</section>





</body>
</html>