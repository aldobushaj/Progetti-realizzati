//prima di tutto aspetto che la pagina si sia caricata
$(document).ready(function(){
    //recupero l'email
    var email =  $("#u_email").val();

    //Apro la finestra per scrivere una recensione se viene cliccato il pulsante "inserisci recensione"
    $("#addrev").on('click',function(){
        $('#myForm').css("display","block");
    });
    //la chiudo se viene premuto il pulsante "close"
    $("#close").on('click',function(){
        $('#myForm').css("display","none");

    });
    //Questo codice viene eseguito quando viene caricata la classe recensioni.php
    if (window.location.href.indexOf('recensioni.php') > -1) {
        //recupero il nome dell'orologio
        var watch = $('#imagereview').attr('alt');
        
        //serve a caricare i commenti subito appena aperta l'immagine
        loadReview(watch,email); 
    }
      

    //Serve a non duplicare le recensioni distinguendo la fase di invio dei dati con quella di invio completato
    var is_sending = false;

    //se viene premuto il bottone inserisci recensione eseguo il codice dentro la funzione che apre una finestra dove scrivere il commento
    $("#addrev").click(function(){
    
        //se viene premuto il bottone add eseguo il codice dentro la funzione  che aggiunge il commento nel database      
        $("#add").click(function(){

            //Se sto inviando dei dati ritorno false(non faccio niente)
            if (is_sending) return false;
            
            //recupero review utente
            var user_review= $("#user_review").val();

            //invio i dati al file getReviews.php che li gestirà opportunamente, prima verifico che il campo non sia vuoto,altrimenti nell'else gestisco il caso
            if(user_review !== ""){
                //Invo con ajax(tipo POST) i dati
                $.ajax({
                    type: 'POST',
                    url: "assets/php/getReviews.php",
                    data:  {
                    
                        watch: watch,            //nome orologio
                        email: email,           //email
                        review: user_review    //recensione inserita dall'utente

                    },
                    beforeSend: function() {
                        // setto il timeout(indico che sto inviando)
                        is_sending = true;
                    },
                    success: function(data){  // in caso di successo chiudo la finestra di inserimento del commento e aggiungo il commento
                        
                        //cancello il contenuto del div per poi successivamente inserire tutte le recensioni aggiornate con un'opportuna funzione
                        $("#user_review").val("");
                        
                        //chiudo la finestra che permette di aggiungere la recensione
                        $('#myForm').css("display","none");
                        
                        //Setto il placeholder della finestra appena chiusa
                        $("#user_review").attr("placeholder", "Type review..");
                        
                        //carico tutte le recensioni con opportuna chiamata ajax
                        loadReview(watch,email);
            
                    },
                    error: function() { // se si è verificato un errore
                        alert("Si è verificato un errore, prova di nuovo");
                    },
                    complete: function() {
                        //l'invio dei dati è completo, posso settare nuovamente a false la variabile
                        is_sending = false;
                    }
                });

            }else {
                //l'utente preme send senza aver scritto nulla, notifico quindi che si deve per forza scrivere qualcosa
                $("#user_review").attr("placeholder", "Devi scrivere qualcosa prima di aggiungere la recensione!");
                return false;
            }            
        

        });

    });

    function loadReview(watch,email){
    //tramite una chiamata POST ajax carico il contenuto dei commenti dal database
    $.post("assets/php/getReviews.php", {
            
        watch: watch,       //nome orologio
        email: email        //email

    },function(data, status){
        //Se c'è almeno una recensione
        if(data !== 'undefined' && data !== ''&&data !== '[]'){
            //converto in oggetto javascript i dati JSON ricevuti da getReview.php ed eseguo opportune operazioni
            var reviewOb = JSON.parse(data);
            
            //cancello il contenuto del div che contiene le recensioni
            $('#review').html("");
            
            //per poi riaggiungerle con il contenuto aggiornato
            $.each(reviewOb,function(index,item){   //in caso di successo
                $('#review').append('<div class="col-12 "> <h5>'+item.user_email+'</h5> <p class="mbr-text py-3  display-7 ">'+item.review+'</p> </div>');
            });
        }else{
            //Se non c'è nessuna recensione
            $('#review').append("Non ci sono ancora commenti, inseriscine uno tu!!");
        }

    });

    }

});//chiude parentesi documento caricato