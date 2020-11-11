<!--Home page contenete tutti i prodotti aquistabili su AB Watches, cliccando l'immagine di un orologio si va nel file recensioni php che avrà alcune funzionalità 
illustrate nel file  -->
<title> Home Page</title>
<?php include("headerFooter/header.php");
    include_once "../php/dbServer.php";
  
    $num = 1;
    $rows = $conn->query("SELECT * from items"); ?>
    <section class="services1 cid-rytGOPPJmW" id="services1-5">
            <div class="container">
                <div class="row justify-content-center">
                    <!--Title-->
                    <div class="title pb-5 col-12"> 
                        <h2 class="align-left pb-3 mbr-fonts-style display-1">  Prodotti in evidenza<br></h2> 
                    </div>
    <?php foreach($rows as $row){
        if($num <=3){?>
        
                <div class="card col-12 col-md-6 p-3 col-lg-4">
                    <div class="card-wrapper">
                        <div class="card-img">
                            <a href="recensioni.php?name=<?=$row['name']?>">
                                <img src="assets/images/<?=$row['name']?>.png" alt="<?=$row['name']?>" title="<?=$row['name']?>">
                            </a>
                        </div>
                        <div class="card-box">
                            <h4 class="card-title mbr-fonts-style display-5">
                                <?=$row['name']?>
                            </h4>
                            <p class="mbr-text mbr-fonts-style display-7"><?=$row['description']?></p>
                            
                            <div class="mbr-section-btn align-left"><p class="mbr-text cost mbr-fonts-style m-0 display-5">€ <?=$row['price']?></p></div>
                        </div>
                    </div>
                </div>
        <?php }else if($num == 4){?>
            </div> </div> </section>
                <section class="services6 cid-rytHfcYyWL" id="services6-6">  
                    <div class="container">
                        <div class="row">
                            <!--Titles-->
                            <div class="title col-12">
                                <h2 class="align-left mbr-fonts-style m-0 display-1">
                                    Lista prodotti
                                </h2>  
                            </div>
                            <div class="card col-12 pb-5">
                                <div class="card-wrapper media-container-row media-container-row">
                                    <div class="card-box">
                                        <div class="row">
                                            <div class="col-12 col-md-2">
                                                <!--Image-->
                                                <div class="mbr-figure">
                                                    <a href="recensioni.php?name=<?=$row['name']?>">
                                                        <img src="assets/images/<?=$row['name']?>" alt="<?=$row['name']?>" title="<?=$row['name']?>">
                                                    </a>
                                                </div>
                                            </div>
                                            <div class="col-12 col-md-10">
                                                <div class="wrapper">
                                                    <div class="top-line pb-3">
                                                        <h4 class="card-title mbr-fonts-style display-5"><?=$row['name']?></h4>
                                                        <p class="mbr-text cost mbr-fonts-style m-0 display-5">€ <?=$row['price']?></p>
                                                    </div>
                                                    <div class="bottom-line">
                                                        <p class="mbr-text mbr-fonts-style display-7"><?=$row['description']?></p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        

        <?php }elseif($num > 4 ){?>
            <div class="card col-12 pb-5">
                    <div class="card-wrapper media-container-row media-container-row">
                        <div class="card-box">
                            <div class="row">
                                <div class="col-12 col-md-2">
                                    <!--Image-->
                                    <div class="mbr-figure">
                                        <a href="recensioni.php?name=<?=$row['name']?>">
                                            <img src="assets/images/<?=$row['name']?>" alt="<?=$row['name']?>" title="<?=$row['name']?>">
                                        </a>
                                    </div>
                                </div>
                                <div class="col-12 col-md-10">
                                    <div class="wrapper">
                                        <div class="top-line pb-3">
                                            <h4 class="card-title mbr-fonts-style display-5"><?=$row['name']?></h4>
                                            <p class="mbr-text cost mbr-fonts-style m-0 display-5">€ <?=$row['price']?></p>
                                        </div>
                                        <div class="bottom-line">
                                            <p class="mbr-text mbr-fonts-style display-7"><?=$row['description']?></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        <?php }$num++;
    }
    $num=1;?>
    </div></div></section>
<?=include("headerFooter/footer.html"); ?>