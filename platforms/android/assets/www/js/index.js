var usuarioLogueado = null;
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
                        usuarioLogueado = data;
                        console.log("USUARIO LOGUEADO "+usuarioLogueado);
                        //Cambia de página
                        mainView.router.load({
                            pageName: 'listaMinerales',
                            reload: true,
                            ignoreCache: true
                        });
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

framework7.onPageInit("listaMinerales", function(){
    $.ajax({
        url: 'https://minerales.herokuapp.com/minerales/'+usuarioLogueado._id,
        type: 'GET',
        dataType: 'json',
        success: function(data){
            console.log("SUCESS");
            console.log(data);
            if(typeof data[0].minerales === "undefined"){
                framework7.addNotification({
                    message: 'No se ha registrado ningún mineral.',
                    hold: 4000})
                }
                else{
                    console.log(data[0].minerales);
                    framework7.virtualList('#listaMinerales', {
                        items: data[0].minerales,
                        template: '<li class="swipeout">'+
                                    '<div class="swipeout-content">'+
                                        '<a href="#" class="item-link item-content">'+
                                            '<div class="item-media"></div>'+
                                            '<div class="item-inner">'+
                                                '<div class="item-title">{{nombre}}</div>'+
                                                '<div class="item-after"><span class="badge">{{densidad}}</span></div>'+
                                            '</div>'+
                                        '</a>'+
                                    '</div>'+
                                    '<div class="swipeout-actions-right">'+
                                        '<a class="bg-green" href="#">Modificar</a>'+
                                        '<a onclick=\'app.eliminarMineral(\"{{codigo}}\");\' href="#" class="swipeout-delete"'+
                                        '>Delete</a>'+
                                    '</div>'+
                                '</li>'
                    });
                }
            },
            error: function(jqXHR, textStatus, errorThrown){
                console.log("ERROR");
                console.log(jqXHR.status + "\n" + textStatus + "\n" + errorThrown);
                framework7.addNotification({
                    message: 'Ha ocurrido un problema al buscar los minerales de la BD.',
                    hold: 4000})
                }
            });

        });

var app = {
    initialize: function() {
        document.addEventListener("deviceready", this.init, false);
    },
    init: function() {

    },
    eliminarMineral: function(id){
        console.log('https://minerales.herokuapp.com/mineral/'+usuarioLogueado._id+'/'+id);
        framework7.confirm('¿Está seguro de que desea eliminar el mineral?', 'Eliminar mineral', function () {
            $.ajax({
                url: 'https://minerales.herokuapp.com/mineral/'+usuarioLogueado._id+'/'+id,
                type: 'DELETE',
                success: function(){
                    framework7.addNotification({
                        message: 'Mineral elimando con éxito.',
                        hold: 4000})
                    },
                    error: function(jqXHR, textStatus, errorThrown){
                        console.log("ERROR");
                        console.log(jqXHR.status + "\n" + textStatus + "\n" + errorThrown);
                        framework7.addNotification({
                            message: 'Ha ocurrido un problema durante la eliminación del mineral.',
                            hold: 4000})
                        }
                    });
                });
    }
};
