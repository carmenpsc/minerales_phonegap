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
                        hold: 4000})
                    }
                    else{
                        framework7.addNotification({
                            message: 'Inicio de sesión correcto.',
                            hold: 4000});

                        //Se almacena el usuario logueado
                        usuarioLogueado = data;
                        console.log("USUARIO LOGUEADO "+usuarioLogueado);

                        //Cambia de página a listado de minerales de cada administrador
                        mainView.router.load({
                            pageName: 'listaMinerales',
                            force: true,
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

/*
    Cada vez que se inicia la página de listado de minerales
    se podrá visualizar la lista de minerales asociada a cada administrador.
    Para ello y mediante ajax phonegap se comunica con la API rest y se crea una lista virtual dinamica
    con el nombre y densidad de cada mineral.
    Al mover cada mineral a la izquierda se verán dos botones: Modificar y Eliminar.
*/
framework7.onPageBeforeAnimation("listaMinerales", function(){
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
                                        '>Eliminar</a>'+
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
    /*
        Este método se llama al hacer click en el botón ELIMINAR de cada una de las
        filas de la lista de minerales, se accioná un modal que pregunta si se desea eliminar dicho mineral
        y se conecta con la API rest para llevar a cabo esta función.
        Se pasa el ID del mineral a eliminar.
    */
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
