$(document).ready(function(){
    $("#subTypeAdditionButton").click(function(){
        $("#subtypeInputs").append("<div class='input-group subTypeInputDiv'><input type='text' class='form-control subTypeVal' placeholder='Enter sub type'><div class='input-group-append'><div class='input-group-text subTypeValInputGroupBtn'><i class='fa fa-minus-circle'></i></div></div></div>");
    });

    $(document).on('click','.subTypeValInputGroupBtn', function(e){
        e.preventDefault();
        $(this).parent().parent().remove();
    });

    $("#typeAdditionForm").submit(function(e){
        e.preventDefault();
        var subTypes = [];
        $(".subTypeVal").each(function(){
            subTypes.push($(this).val());
        });
        var data = {type: $("#typeVal").val(), subtypes: subTypes}
        $.ajax({
            type: "POST",
            url: '/rest/create/type',
            contentType: 'application/json',
            processData: false,
            data: JSON.stringify(data),
            success: function(result){
                if(result === 'success'){location.href = '/item/section/modify';}
            },error: function (jqXHR, textStatus, errorThrown){
            }
        });
    });

    $("#attributeAdditionForm").submit(function(e){
        e.preventDefault();
        $.ajax({
            type: "POST",
            url: '/rest/create/attribute/'+$("#attributeVal").val(),
            contentType: 'application/json',
            processData: false,
            success: function(result){
                if(result === 'success'){location.href = '/item/section/modify';}
            },error: function (jqXHR, textStatus, errorThrown){
            }
        });
    });
});
function deleteAttribute(data,el){
    //alert(data);
    $.ajax({
        type: "DELETE",
        url: '/rest/delete/attribute/'+data,
        contentType: 'application/json',
        processData: false,
        success: function(result){
            //alert(result);
            //Todo: Show spinner
            $(el).remove();
        },error: function (jqXHR, textStatus, errorThrown){
        }
    });
}
function deleteType(data,el){
    //alert(data);
    $.ajax({
        type: "DELETE",
        url: '/rest/delete/type/'+data,
        contentType: 'application/json',
        processData: false,
        success: function(result){
            //alert(result);
            //Todo: Show spinner
            $(el).parent().parent().remove();
        },error: function (jqXHR, textStatus, errorThrown){
        }
    });
}