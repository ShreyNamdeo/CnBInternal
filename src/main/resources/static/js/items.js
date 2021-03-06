$(document).ready(function(){
    getAllItems();
});
function getAllItems(){
    $.ajax({
        type: "GET",
        url: '/rest/item/list?limit=10',
        contentType: 'application/json',
        processData: false,
        success: function(result){
            if(result.list.length > 0){
                for(var i=0 ; i<result.list.length; i++){
                    //alert(result[i].name);
                    var itemAttributesAndVals = "";
                    if(result.list[i].itemAttributes.length > 0){
                        for(var j=0; j<result.list[i].itemAttributes.length; j++){
                            itemAttributesAndVals = itemAttributesAndVals + "["+result.list[i].itemAttributes[j].itemAttributeId +" : "+result.list[i].itemAttributes[j].value+"] ";
                        }
                    }
                    var firstImageUrl = "https://sisterhoodofstyle.com/wp-content/uploads/2018/02/no-image-1.jpg";
                    var moreImages = "0 images"
                    if(result.list[i].images.length > 0){
                        firstImageUrl = result.list[i].images[0].readUrl;
                        var moreImageCount =  result.list[i].images.length-1;
                        moreImages = " +"+moreImageCount + " images";
                    }
                    //alert(moreImages);
                    $("#item-list").append("<tr class='itemRow'><th scope='row'>"+result.list[i].id+"</th><td>"+result.list[i].name+"</td><td>"+result.list[i].type.type+"</td><td>"+result.list[i].type.subType+"</td><td>"+result.list[i].price+"</td><td>"+itemAttributesAndVals+"</td><td><img src="+firstImageUrl+" height=20 width=20/> <small>"+moreImages+"</small> </td><td class='text-center'><a href='/item?item="+result.list[i].id+"' style='cursor:pointer'><i class='fa fa-edit'></i></a> &nbsp; <a href='javascript:void(0)' onclick='deleteItem("+result.list[i].id+", this)' style='cursor:pointer'><i class='fa fa-trash'></i></a></td></tr>");
                }
            }
        },error: function (jqXHR, textStatus, errorThrown){
        }
    });
}
function deleteItem(data, el){
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
            getAllItems();
        },error: function (jqXHR, textStatus, errorThrown){
        }
    });
}