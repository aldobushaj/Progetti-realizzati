// Questo file gestisce tutte le operazioni della lista preferiti
// Prima di tutto aspetto che il documento si sia caricato
$(document).ready(function(){
   var email =  $("#u_email").val();    //recupero l'email
    
    //Questo codice viene eseguito quando viene caricata la classe preferiti.php
    if(window.location.href.indexOf('preferiti.php') > -1){
        //Carico i preferiti
        loadFavorite();
    }

    // Devo eliminare l'orologio selezionato, quindi mi collego al database, lo elimino e infine carico la lista aggiornata di prodotti.
    $('#favorite_list').on('click','a',function(){

        $.post("assets/php/getFavorite.php", {
            
            email: email,           //email
            watch: this.name,      //nome orologio
            add : 0               //indico che è stato premuto il pulsante per eliminare l'articolo

        },function(data, status){
            //Carico semplicemente la lista dei preferiti(che sarà uguale a quella di prima meno l'elemento appena eliminato)
            loadFavorite();
        });
    });


    //E stato cliccato il bottone che ha il compito di aggiungere ai preferiti l'orologio selezionato
    $('#addfav').on('click',function(){
        
        $.post("assets/php/getFavorite.php", {
            
            email: email,         //email
            watch: this.name,    //nome orologio
            add : 1             //indica che sono nel caso in cui devo aggiungere l'orologio alla lista dei preferiti

        },function(data, status){
            //Se l'orologio è già presente "data" conterrà un messaggio che lo notifica all'utente opportunamente
            //quindi basta verificare che non è stato riscontrato questo messaggio
            if(data == 'undefined' || data == '' || data == '[]'){
                //Notifico graficamente l'utente che l'aritcolo è stato aggiunto alla lista preferiti
                $("#rev_fav").attr('class', 'mbri-success mbr-iconfont mbr-iconfont-btn');

                //Disabilito il pulsante in modo da non rendere possibile cliccare nuovamente, onde evitare di inviare al database la richiesta(con stessa risposta)
                $("#addfav").prop("disabled",true);

                // carico la lista dei preferiti          
                loadFavorite();

            }else{                
                //notifico all'utente che è gia stato aggiunto con il messaggio ricevuto dal file php
                $("#addfav").text(data);
                //E disabilito il pulsante in modo da non rendere possibile cliccare nuovamente, onde evitare di inviare al server la richiesta(con stessa risposta)
                $("#addfav").prop("disabled",true);
                
                return false;
            }   

        });
    });


    //Funzione che carica la lista dei prodotti nella lista preferiti
    function loadFavorite(){
        $.post("assets/php/getFavorite.php", {
            
            email: email        //email
    
        },function(data, status){
            //Se c'è almeno un orologio nella lista preferiti
            if( data !== 'undefined' && data !== ''&&data !== '[]'){
                // Converto in oggetto javascript i dati JSON  ricevuti da getReview.php ed eseguo le opportune operazioni
                var reviewOb = JSON.parse(data);                
                // Cancello il contenuto del div che contiene gli articoli nella lista preferiti
                $('#favorite_list').html("");
                
                //per poi riaggiungerle con il contenuto aggiornato
                $.each(reviewOb,function(index,item){   
                    $('#favorite_list').append(getFavoriteDiv(item.favorite,item.price));
                });
            }else{  // in questo caso non c'è nessun orologio nei preferiti
                
                // Quindi cancello tutto
                $('#favorite_list').html("");
                // e avverto l'utente che non è presente nessun articolo ai preferiti
                $('#favorite_list').append("Non ci sono ancora elementi nei preferiti, niente di interessante?");
                
                //Modifico il css in modo da riempire l'intera finestra
                $('#favorite_list').css({ padding: "57px"}); 
            }
    
        });
    }

    //funzione che restituisce gli opportuni div da aggiungere alla lista degli articoli preferiti
    function getFavoriteDiv(watch,price){
        //div contenete l'immagine
        var image = '<div class="col-2 col-md-2 my-3">\
                        <div class="mbr-figure">\
                            <a href="recensioni.php?name='+watch+'">\
                            <img src="assets/images/'+watch+'.png" alt="'+watch+'" title="'+watch+'">\
                            </a>\
                        </div>\
                    </div>';
        //div che contiene il contenuto(prezzo.nome,ecc.)
        var content = '<div class="col-12 col-md-10">\
                            <div class="wrapper">\
                                <div class="top-line pb-3">\
                                    <h4 class=" display-5">'+watch+'</h4>\
                                    <p class="display-5">€ '+price+'</p>\
                                </div>\
                                <div class="container">\
                                        <div class="media-container-row title">\
                                            <div class="col-12 col-md-8">\
                                                <div class="mbr-section-btn align-center"><a class="btn btn-black-outline display-4" name="'+watch+'">Elimina</a>\
                                                    <button name="'+watch+'" class="btn btn-primary display-4" >Sposta nel carrello</button></div>\
                                            </div>\
                                        </div>\
                                </div>\
                            </div>\
                        </div>';
        return image+""+content;
   }


});//chiude parentesi documento caricato    