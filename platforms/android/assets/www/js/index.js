//En esta variable se almacena el usuario que esta actualmente logueado
var usuarioLogueado = null;

//Se inicializa framework7 con su configuración correspondiente
var $$ = Dom7;
framework7 = new Framework7({
    material: true,
    swipePanel: 'left'
});

var mainView = framework7.addView('.view-main', {
    dynamicNavbar: true,
    domCache: true
});

/*
    Cada vez que se inicia la página de registro
    se abrirá un formulario donde se añadirán los campos para
    registrar un nuevo adminsitrador.
    El botón del formulario mediante ajax se comunica con la API rest para
    añadir dicho usuario adminsitador al sistema.
    Antes se verifica que el correo que se introduce no exista ya en el sistema.
*/
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
                                hold: 4000});
                            mainView.router.loadPage({pageName: 'login', ignoreCache: true, force: true});

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

/*
    Cada vez que se inicia la página de login
    se abrirá un formulario donde se introducirán las credenciales del administrador.
    El botón del formulario mediante ajax se comunica con la API rest para
    verificar que ese usuario administrador esta registrado en el sistema.
    Antes se verifica que el correo  y la contraseña que se introducen son correctas.
*/
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
                        hold: 4000});
                    }
                    else{
                        framework7.addNotification({
                            message: 'Inicio de sesión correcto.',
                            hold: 4000});

                        //Se almacena el usuario logueado
                        usuarioLogueado = data._id;
                        console.log("USUARIO LOGUEADO "+usuarioLogueado);

                        //Acomodo el menu
                        $('#registro').hide();
                        $('#listadoM').show();

                        //Me comunico con android
                        app.addItem();
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
        if(usuarioLogueado === null){
            $('#listadoM').hide();
        }
    },
    addItem: function() {
         HybridBridge.addItem(usuarioLogueado, "com.minerales.ListaMineralesActivity", function(){console.log("Hybrid Bridge Success")},function(e){console.log("Hybrid Bridge Error" + e)});
     }

};
