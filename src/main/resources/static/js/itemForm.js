$(document).ready(function(){

    $("#submitItemAttributes").submit(function(e){
        e.preventDefault();
        var itemAttributes = [];
        $(".attributes").each(function(index){
            //alert($(this).find(".key").val()+" "+$(this).find(".value").val());
            itemAttributes.push({itemAttributeId : $(this).find(".key").val(), value : $(this).find(".value").val()});
        });
        var itemDto = { itemAttributes : itemAttributes };
        //alert(JSON.stringify(itemDto));
        $.ajax({
            type: "POST",
            url: '/rest/itemAttributes',
            contentType: 'application/json',
            processData: false,
            data: JSON.stringify(itemDto),
            success: function(result){
                if(result === 'success'){location.href = '/item/media';}
            },error: function (jqXHR, textStatus, errorThrown){
            }
        });
    });

    $(document).on("click",".itemType", function(e){
        //alert($(this).children(".itemTypeVal").attr("id"));
        $(".itemType").removeClass("active");
        if(!$(this).hasClass("active")){
            $("#subType").val(null);
        }
        $(".itemType").children("span").children(".fa").removeClass("fa-check");
        $(".itemType").children("span").children(".fa").addClass("fa-plus");
    });

    $(document).on("click",".itemSubType", function(e){
        //alert($(this).children(".itemTypeVal").attr("id"));
        $(".itemSubType").removeClass("active");
        $(this).addClass("active");
        $(".itemSubType").children("span").children(".fa").removeClass("fa-check");
        $(".itemSubType").children("span").children(".fa").addClass("fa-plus");
        $("#subType").val($(this).children(".itemSubTypeVal").attr("id"));
        $(this).children("span").children(".fa").removeClass("fa-plus");
        $(this).children("span").children(".fa").addClass("fa-check");
    });

    $('.itemTypeVal').each(function(i, obj) {
        if($(this).text().toLowerCase().trim() === $("#type").val().toLowerCase()) {
            //alert("Found the element");
            $(this).next().find(".fa").removeClass("fa-plus");
            $(this).next().find(".fa").addClass("fa-check");
            $(this).parent().addClass("active");
            getAllSubtypes($(this).text().trim(), $(this));
            return false;
        }
    });

});
function addAttributeToItem(data){
    $("#itemAttributes").append("<div class='form-group attributes' id='"+data+"listItem'>"+
        "<div class='row itemAttrRow'>"+
             "<div class='col-3'>"+
                 "<label class='baseline-middle'>"+data+"</label>"+
                 "<input class='key' type='hidden' value='"+data+"'>"+
             "</div>"+
             "<div class='col-8'>"+
                 "<input type='text' class='form-control value' placeholder='Enter value'>"+
             "</div>"+
             "<div class='col-1 delete-attr-entry' onclick=\"deleteSelection(\'"+data+"listItem\')\">"+
                 "<i class='fa fa-minus-circle baseline-middle larger-font'></i>"+
             "</div>"+
        "</div>"+
    "</div>");
}

function getAllSubtypes(data,el){
    $("#subtypesUL").find(".subTypeElm").remove();
    //alert(data);
    $.ajax({
        type: "GET",
        url: '/rest/item/subtypes/'+data,
        contentType: 'application/json',
        processData: false,
        success: function(result){
            //alert(result);
            $("#subtypesUL").find("li").remove();
            $(el).parent().addClass("active");
            $("#type").val(data);
            $(el).parent().children("span").children(".fa").removeClass("fa-plus");
            $(el).parent().children("span").children(".fa").addClass("fa-check");
            for(var i in result.subtypes){
                if ($("#subType").val() === result.subtypes[i]){
                    $("#subtypesUL").append("<li style='margin-right: 5px;' class='itemAttributeListElement itemSubType active'><span id='"+result.subtypes[i]+"' class='itemAttrVal itemSubTypeVal'>"+result.subtypes[i]+"  </span><span><i class='fa fa-check'></i></span></li>");
                }else{
                    $("#subtypesUL").append("<li style='margin-right: 5px;' class='itemAttributeListElement itemSubType'><span id='"+result.subtypes[i]+"' class='itemAttrVal itemSubTypeVal'>"+result.subtypes[i]+"  </span><span><i class='fa fa-plus'></i></span></li>");
                }
            }
        },error: function (jqXHR, textStatus, errorThrown){
        }
    });
}

function deleteSelection(str){
    var deleteSelector = "#"+str;
    $(deleteSelector).remove();
}

$(function() {

  // We can attach the `fileselect` event to all file inputs on the page
  $(document).on('change', ':file', function() {
    var input = $(this),
        numFiles = input.get(0).files ? input.get(0).files.length : 1,
        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label]);
  });

  // We can watch for our custom `fileselect` event like this
  $(document).ready( function() {
      $(':file').on('fileselect', function(event, numFiles, label) {

          var input = $(this).parents('.input-group').find(':text'),
              log = numFiles > 1 ? numFiles + ' files selected' : label;

          if( input.length ) {
              input.val(log);
          } else {
              if( log ) alert(log);
          }

      });
  });

});

function deleteMediaObject(data,el){
    //alert(data);
    $.ajax({
        type: "DELETE",
        url: '/rest/item/media/delete/'+data,
        contentType: 'application/json',
        processData: false,
        success: function(result){
            //alert(result);
            //Todo: Show spinner
            $(el).parent().parent().parent().parent().remove();
        },error: function (jqXHR, textStatus, errorThrown){
        }
    });
}