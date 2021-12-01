<!--Qui si accede a tutte le informazioni di uno specifico orologio, si può accedere allle sue recensioni(se ce ne sono),
si ha la possibilità di aquistare, mettere nei preferiti o aggiungere una recensione-->
<?php include("headerFooter/header.php");?>

<title> Recensioni</title>

<section class="tabs4 cid-ryFav28bUo" id="tabs4-z">
    <!---->
    
    <!---->
    <!--Overlay-->

    <div class="container ">
        <!-- Il titolo è dato dal nome dell'orologio passato come query string-->
        <h2 class="mbr-section-title align-center pb-3 mbr-fonts-style display-1 mt-3"><?= $_GET['name']?></h2>
       
        
        <div class="media-container-row mt-5 pt-3">
            <div class="mbr-figure" style="width: 39%;">
                <!-- Viene settata l'immagine corretta con il nome ricevuto dalla query string-->
                <img id="imagereview" src="assets/images/<?= $_GET['name']?>.png" alt="<?= $_GET['name']?>">
            </div>
            <div class="tabs-container col-10">
                <ul class="nav nav-tabs " role="tablist">

                    <!--Questo bottone ha il compito di aggiungere le recensioni,-->                   
                    <button id ="addrev" class="open-button btn btn-sm btn-primary-outline display-4"  >Inserisci recensione</button>
                    
                    <!--questo ha il compito di aggiungere l'orologio selezionato alla lista dei preferiti,-->                   
                    <button id = "addfav" name="<?= $_GET['name']?>" class="btn btn-sm btn-primary-outline display-4">
                        <span id = "rev_fav" class="mbri-hearth mbr-iconfont mbr-iconfont-btn"></span>
                    </button>

                    <!--infine questo aggiunge l'articolo selezionato al carrello-->                   
                    <button id = "addshop" name="<?= $_GET['name']?>" class="btn btn-sm btn-primary-outline display-4" >
                        <span id="changeShop" class="mbri-cart-add mbr-iconfont mbr-iconfont-btn"></span>
                    </button>

                         
                </ul>
            
                
                
            <!-- Div in cui verranno aggiunte tutte le recensioni-->
            <div id="review" class="container mt-5"></div>
                


                
            </div>
        </div>
    </div>



    
<!--Div che gestisce l'apertura del popup che permette di inserire una recensione -->                   
<div class="chat-popup" id="myForm">
    <div class="div-container">
        <h1>Recensione</h1>
        
        <!-- Textarea contenente la recensione inserita dall'utente -->                           
        <textarea id="user_review" placeholder="Scrivi la recensione.." name="msg"></textarea>
        
        <!-- Questo bottone ha il compito di aggiungere la recensione appena scritta nella lista delle recensioni dell'orologio, -->                           
        <button id="add" type="submit" class="btn">Add</button>
        <!-- mentre questo chiude semplicemente il popup nel caso di un ripensamento dell'utente-->                           
        <button id="close" type="button" class="btn cancel">Close</button>
    </div>
</div>

</section>


<?php include("headerFooter/footer.html"); ?>
