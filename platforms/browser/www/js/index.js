var $$ = Dom7;

framework7 = new Framework7({
    material: true,
    swipePanel: 'left'
});

var mainView = framework7.addView('.view-main', {
    dynamicNavbar: true,
    domCache: true
});

framework7.onPageInit("registro", function(){
    $$('#registroButton').on('click', function(event){
        event.preventDefault();
        var formdata =  $('#registroForm').serialize();
        $.ajax({
            url: 'https://minerales.herokuapp.com/registro',
            type: 'POST',
            dataType: 'json',
            data: formdata,
            success: function(data){
                console.log("SUCESS");
                console.log(data);
                if(data == null){
                    $.ajax({
                        url: 'https://minerales.herokuapp.com/usuarios',
                        type: 'POST',
                        dataType: 'json',
                        data: formdata,
                        success: function(data){
                            console.log("SUCESS");
                            console.log(data);
                            framework7.addNotification({
                                message: 'Registro completado con éxito.',
                                hold: 4000})
                        },
                        error: function(jqXHR, textStatus, errorThrown){
                            console.log("ERROR");
                            console.log(jqXHR.status + "\n" + textStatus + "\n" + errorThrown);
                            framework7.addNotification({
                                message: 'Ha ocurrido un problema durante el registro.',
                                hold: 4000})
                        }
                    });
                }
                else{
                    framework7.addNotification({
                        message: 'Este correo electrónico ya está en uso.',
                        hold: 4000})
                }
            },
            error: function(jqXHR, textStatus, errorThrown){
                console.log("ERROR");
                console.log(jqXHR.status + "\n" + textStatus + "\n" + errorThrown);
                framework7.addNotification({
                    message: 'Ha ocurrido un problema durante el registro.',
                    hold: 4000})
            }
        });
    });
});



framework7.onPageInit("login", function(){
    $$('#loginButton').on('click',function(event){
        event.preventDefault();
        var formdata =  $('#loginForm').serialize();
        $.ajax({
            url: 'https://minerales.herokuapp.com/login',
            type: 'POST',
            dataType: 'json',
            data: formdata,
            success: function(data){
                console.log("SUCESS");
                console.log(data);
                if(data == null){
                    framework7.addNotification({
                        message: 'Usuario o contraseña incorrectos.',
                        hold: 4000})
                    }
                    else{
                        framework7.addNotification({
                            message: 'Inicio de sesión correcto.',
                            hold: 4000});
                        mainView.router.loadPage('listaMinerales.html');
                    }
                },
                error: function(jqXHR, textStatus, errorThrown){
                    console.log("ERROR");
                    console.log(jqXHR.status + "\n" + textStatus + "\n" + errorThrown);
                    framework7.addNotification({
                        message: 'Ha ocurrido un problema verificando los datos de acceso.',
                        hold: 4000})
                }
            });
        });
    });

    var app = {
        initialize: function() {
            document.addEventListener("deviceready", this.init, false);
        },
        init: function() {

        }
    };
