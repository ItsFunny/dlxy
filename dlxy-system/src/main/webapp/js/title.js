function del(obj){
		var titleId=$(obj).attr("titleId");
		alert(titleId);
		jQuery.get('/api/v1/admin/title/delete.html',{"titleId":titleId},function(data,textStatus,jqXHR){
			alert(data.msg);
			location.reload();
		});
	}
    function beforeSave(obj,isAddChild,isUpdate){
    	if(isUpdate){
    		$("#titleId").val($(obj).attr("titleId"));
    		$("#titleParentId").val("");
    	}
    	if(isAddChild){
    		$("#titleParentId").val($(obj).attr("titleId"));
    		$("#titleId").val("");
    	}
    }
    function addOrUpdate(){
    	var params=$("#titleForm").serialize();
    	jQuery.post('/api/v1/admin/title/addOrUpdate.html',params,function(data,textStatus,jqXHR){
			alert(data.msg);
			location.reload();
    	},'json'); 
    }