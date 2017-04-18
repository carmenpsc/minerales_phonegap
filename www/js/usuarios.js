$(document).ready(function(){
    $("#registroForm").submit(function(event){
        event.preventDefault();
        var formdata =  $('#registroForm').serialize();
        $.ajax({
            url: 'https://minerales.herokuapp.com/usuarios',
            type: 'POST',
            dataType: 'json',
            data: formdata,
            success: function(data){
                console.log("SUCESS");
                console.log(data);
                $("#notificaciones").append('<div class="alert alert-success" role="alert">Registro completado con éxito</div>');
            },
            error: function(jqXHR, textStatus, errorThrown){
                console.log("ERROR");
                console.log(jqXHR.status + "\n" + textStatus + "\n" + errorThrown);
                $("#notificaciones").append('<div class="alert alert-error" role="alert">Ha ocurrido un problema durante el registro</div>');
            }
        });
    });
    $("#loginForm").submit(function(event){
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
                if(data == null)
                $("#notificaciones").append('<div class="alert alert-warning" role="alert">Correo electrónico o contraseña incorrecta</div>');
                else
                $("#notificaciones").append('<div class="alert alert-success" role="alert">Login completado con éxito</div>');
            },
            error: function(jqXHR, textStatus, errorThrown){
                console.log("ERROR");
                console.log(jqXHR.status + "\n" + textStatus + "\n" + errorThrown);
                $("#notificaciones").append('<div class="alert alert-error" role="alert">Ha ocurrido un problema verificando los datos de acceso</div>');
            }
        });
    });
});
