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
                        //Cambia de página
                        mainView.router.loadPage({
                            url: 'listaMinerales.html',
                            reload: true,
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
        url: 'https://minerales.herokuapp.com/minerales',
        type: 'GET',
        dataType: 'json',
        success: function(data){
            console.log("SUCESS");
            console.log(data);
            if(data.length === 0){
                framework7.addNotification({
                    message: 'No se ha registrado ningún mineral.',
                    hold: 4000})
                }
                else{
                    var myList = framework7.virtualList('#listaMinerales', {
                        items: data,
                        template: '<li class="swipeout">'+
                                    '<div class="swipeout-content">'+
                                        '<a href="#" class="item-link item-content">'+
                                            '<div class="item-media"><i class="icon icon-f7">camera_fill</i></div>'+
                                            '<div class="item-inner">'+
                                                '<div class="item-title">{{nombre}}</div>'+
                                                '<div class="item-after"><span class="badge">{{densidad}}</span></div>'+
                                            '</div>'+
                                        '</a>'+
                                    '</div>'+
                                    '<div class="swipeout-actions-right">'+
                                        '<a class="bg-green" href="#">Modificar</a>'+
                                        '<a onclick="app.confirmarBorrado("'+{{_id}}+'");" href="#" class="swipeout-delete"'+
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
    confirmarBorrado: function(id){
        framework7.confirm('¿Está seguro de que desea eliminar el mineral?', 'Eliminar mineral', function () {
            this.eliminarMineral(id);
        });
    },
    eliminarMineral: function(id){
        var miId = "'"+id+"'";
        $.ajax({
            url: 'https://minerales.herokuapp.com/mineral/'+miId,
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
    }
};
