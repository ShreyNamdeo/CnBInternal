$(document).ready(function(){
    getDashboardItems();
});
function getDashboardItems(){
    $.ajax({
        type: "GET",
        url: '/rest/item/list?limit=5',
        contentType: 'application/json',
        processData: false,
        success: function(result){
            if(result.list.length > 0){
                for(var i=0 ; i<result.list.length; i++){
                    //alert(result[i].name);
                    $("#item-list").append("<tr class='itemRow'><th scope='row'>"+result.list[i].id+"</th><td>"+result.list[i].name+"</td><td>"+result.list[i].type.type+"</td><td>"+result.list[i].type.subType+"</td><td>"+result.list[i].price+"</td><td class='text-center'><a href='/item?item="+result.list[i].id+"' style='cursor:pointer'><i class='fa fa-edit'></i></a> &nbsp; <a href='javascript:void(0)' onclick='deleteItem("+result.list[i].id+", this)' style='cursor:pointer'><i class='fa fa-trash'></i></a></td></tr>");
                }
            }
        },error: function (jqXHR, textStatus, errorThrown){
        }
    });
}
function deleteItem(data,el){
    $.ajax({
        type: "DELETE",
        url: '/rest/delete/item/'+data,
        contentType: 'application/json',
        processData: false,
        success: function(result){
            //alert(result);
            //Todo: Show spinner
            //$(el).parent().parent().remove();
            $(".itemRow").remove();
            getDashboardItems();
        },error: function (jqXHR, textStatus, errorThrown){
        }
    });
}