//aspetto che il documento si sia caricato
$(document).ready(function(){  
    //Recupero i div dei bottoni e delle info sugli articoli
    var dataDefault = getDefault(1);
    //Recupero l'email
    var email =  $("#email").val();
    //Variabile che distingue primo elemento del carrello dagli altri(per aggiungere informazioni su nome, prezzo e numero articoli)name, price ,nr
    var num = 0;
    //Questo codice viene eseguito quando viene caricata la classe preferiti.php
    if(window.location.href.indexOf('carrello.php') > -1){
        //Carico il carrello
        loadShoppingCart();
    }
    

    //è stato cliccato il pulsante(in recensioni.php) che aggiunge il prodotto al carrello
    $("#addshop").on('click',function(){
        $.post("assets/php/getShoppingCart.php", {
            
            email: email,         //email
            watch: this.name,    //indico che è stato premuto il pulsante "aggiungi al carrello"
            add : 1             //indica che sono nel caso in cui devo aggiungere l'orologio al carrello

        },function(data, status){
            //se ho un solo articolo visualizzo il numero di elementi con la scritta "item"
            if(Number(data) == 1) $("#addshop").text(data+" item");
            // altrimenti scrivo "item's"
            else $("#addshop").text(data+" item's");
        });
    });



    $('#item').on('click','button',function(){
        $.post("assets/php/getShoppingCart.php", {
            
            email: email,           //email
            watch: this.name       //indico che è stato premuto il pulsante per eliminare l'articolo

        },function(data, status){
            console.log(data);
            //Carico semplicemente la lista dei preferiti(che sarà uguale a quella di prima meno l'elemento appena eliminato)
            loadShoppingCart();
        });
    });

    //E' stato  cliccato il bottone che sposta l'orologio dalla lista preferiti al carrello
    $("#favorite_list").on('click','button',function(){
        //recupero il nome dell'orologio
        var name = this.name;
        //lo aggiungo al carrello tramite una chiamata AJAX
        $.post("assets/php/getShoppingCart.php", {
            
            email: email,        //email
            watch: name,        //indico che è stato premuto il pulsante "aggiungi al carrello"
            add : 1            //indica che sono nel caso in cui devo aggiungere l'orologio al carrello

        },function(data, status){
            //In questo caso l'articolo è stato aggiunto con successo al carrello quindi devo eliminarlo dalla lista preferiti tramite una chiamata AJAX
            $.post("assets/php/getFavorite.php", {
            
                email: email,           //email
                watch: name,           //nome orologio
                add : 0               //valore che indica che bisogna eliminare l'articolo
    
            },function(data, status){
                //apro la pagina che mostra il carrello
                open("carrello.php",'_self');
            });

        });
    });

    $('#item').on('click','a',function(){
        //Distinguo il click effettuato da details(che apre la pagina recensioni prodotto) da quella in cui vengono acquistati controllando l'id
        if(this.id != "details"){
            $.post("assets/php/getShoppingCart.php", {
                
                email: email,           //email
                add:0                  //indico che è stato acquistato il contenuto nel carrello

            },function(data, status){
                //vado alla home e notifico l'utente che l'acquisto è andato a buon fine
                open("index.php?status=items_buyed",'_self');
            });
        }
    });

    //funzione che carica la lista dei prodotti nel carrello
    function loadShoppingCart(){
        
        $.post("assets/php/getShoppingCart.php",{
            
            email: email       //email
    
        },function(data, status){
            //variabile che conterrà il prezzo totale degli orologi
            var tot = 0;
            //Se c'è almeno un orologio nel carrello
            if( data !== 'undefined' && data !== ''&&data !== '[]'){
                //converto in oggetto javascript i dati JSON  ricevuti da getShoppingCart.php ed eseguo le opportune operazioni
                var reviewOb = JSON.parse(data);                
                //cancello il contenuto del div che contiene gli articoli nel carrello
                $('#item').html("");
                //per poi riaggiungerle con il contenuto aggiornato
                
                
                $.each(reviewOb,function(index,item){   //in caso di successo...
                    //calcolo il prezzo totale
                    tot += eval("item.price*item.number_items");
                    //if(tot.leng)
                    // e agginugo gli zeri finali
                    var totWithZeroes = tot.toLocaleString("en",{useGrouping: false,minimumFractionDigits: 3});
                    //quindi aggiorno il campo "prezzo totale" del div contenente il prezzo totale
                    dataDefault = getDefault(totWithZeroes);
                    //in fondo aggiungo il bottone ad ogni ciclo(la funzione che chiamo provvederà a lasciare solo in fondo i bottoni)
                    $('#item').append(getShoppingCartDiv(item.shopping_cart,item.price,item.number_items)+dataDefault.button);
                });
            }else{  //altrimenti se non c'è nessun orologio

                $('#item').html("");
                $('#item').append("Non ci sono ancora elementi nel carrello, coraggio non farteli scappare!");
            }
    
        });
    }

//Funzione che restituisce il div da aggiungere al carrello
function getShoppingCartDiv(watch,price,number){
    //aggiungo gli zeri finali e calcolo il prezzo degli orologii
    var priceWithZeroes = eval("price*number").toLocaleString("en",{useGrouping: false,minimumFractionDigits: 3});

        //Elimino i bottoni aggiunti precedentemente per poi riaggiungerli succesivamente(cosi si troveranno solo all'ultimo elemento )
        $(".shopping_button").remove();
        //div che contiene il contenuto(prezzo.nome,ecc.)
        var content = ' <div id="'+watch+'" class="card-wrapper media-container-row my-3">\
                            <div class="card-box ml-0">\
                                <div class="row">\
                                    <div class="col-md-2">\
                                        <div class="mbr-figure">\
                                            <a id="details" href="recensioni.php?name='+watch+'">\
                                            <img href="recensioni.php?name='+watch+'" src="assets/images/'+watch+'.png" alt="'+watch+'">\
                                            </a>\
                                        </div>\
                                    </div>\
                                    <div class="col-md-10">\
                                        <div class="wrapper">\
                                            <div class="top-line pb-3">\
                                                <p class="mbr-text display-5 col-5">'+watch+'</p>\
                                                <p class="mbr-text display-5 col-5 ml-5">€ '+priceWithZeroes+'</p>\
                                                <p id="num_item" class="mbr-text display-5 col-2">X'+number+'</p>\
                                                <button class="btn mbri-trash btn-white text-black display-5" name ="'+watch+'"></button>\
                                            </div>\
                                        </div>\
                                    </div>\
                                </div>\
                            </div>\
                        </div>';
        if(num==0){//indica che è il primo elemento quindi oltre al contenuto inserisco anche le informazioni
            num++;
            return dataDefault.info+content;
        }
        else return content;
    
}
    //funzione che restituisce i div di default per il bottone e le info dei prodotti
    function getDefault(totalPrice){
        var dataDefault = {
            info: '<div class="row">\
                        <div class="title col-6 ml-5">\
                            <p class="align-left mbr-fonts-style m-0 display-5"><strong>Name</strong></p>\
                        </div>\
                        <div class="title col-2 ">\
                            <p class="align-middle mbr-fonts-style m-0 display-5"><strong>Price</strong></p>\
                        </div>\
                        <div class="title col-3">\
                            <p class="align-right mbr-fonts-style m-0 display-5"><strong>N° item</strong></p>\
                        </div>\
                    </div>',

            button: '<div class="shopping_button">\
                        <div class="media-container-row title">\
                            <div class="col-12 col-md-8">\
                            <textarea readonly class="text-center md-textarea form-control text-center" rows="2">Totale orologi nel carrello : € '+totalPrice+'</textarea>\
                            <div class="mbr-section-btn align-center mt-2">\
                                    <a id="buy" class="btn btn-primary display-4">Acquista</a></div>\
                            </div>\
                        </div>\
                    </div>'
        }
        return dataDefault;
    }


});//chiude parentesi documento caricato    