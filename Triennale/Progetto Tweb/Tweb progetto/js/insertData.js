//Aspetto che il documento si sia caricato
$(document).ready(function(){
  //Apro la finestra del login
  $("#log").click(function(){
    $('#login').css("display","block");
  });

  //Apro la finestra del signup
  $("#sign").click(function(){
    $('#signUp').css("display","block");
  });
  
  
  //Se clicco close chiudo la finestra 
  $('#cancel,span').click(function(){
    $('#login').css("display","none");
    $('#signUp').css("display","none");
  });

});