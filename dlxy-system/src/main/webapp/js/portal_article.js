 
function loadTitles(){
		jQuery.get('/api/v1/aboutTitles.html',function(data){
			 if(data.code==1){
				var str='<li class="bbb" ><a href="/index.html"><i class="icon-home"></i>首页</a></li>';
				titleIdArray=new Array(data.data.length);
			  	for(var i=0;i<data.data.length;i++){
			  		var o=data.data[i];
			  		titleIdArray[i]=o.titleId;
			  		str+='<li class="aaa" id="li_'+o.titleId+'" titleId="'+o.titleId+'""><a href="/title/detail/'+o.titleId+'.html"><i class="icon-home"></i>'+o.titleName+'</a>';
			  		str+='</li>';
			  	}
			  	$("#above_titles").append(str);
			  	$(".aaa").mouseenter(function(obj){
			  		var t=obj.currentTarget;
					loadChildTitles(t);
				})	
				$(".aaa").mouseleave(function(obj){
					var t=obj.currentTarget;
					changeChildsStatus(t,"none");
				})
			  }
		},'json');
	}




function scrollLoadTitles(){
    	var titleId=titleIdArray.shift();
    	if(titleId==null){
    		return;
    	}
    	jQuery.get('/api/v1/scrollLoadTitle/'+titleId+'.html',function(data,textStatus,jqXHR){
    		if(data.code==1 && data.data !=null){
    			var dto=data.data;
    			var str='<div class="kaoshi"><div class="biaoti"><h2>'+dto.titleName+'</h2><a href="/title/detail/'+dto.titleId+'.html">查看详情 +</a></div>';
    			for(var i=0;i<dto.childs.length;i++){
    				var t=dto.childs[i];
    				str+='<ul class="kemu"><div class="kemu_bt"><h2>'+t.titleName+'</h2><a href="xw.html">更多>></a></div>';
    				for(var j=0;j<t.articles.length;j++){
    					var a=t.articles[j];
    					str+=' <li><a href="/article/detail/'+a.articleId+'.html"><i></i>'+a.articleName+'</a></li>';
    				}
    				str+='</ul>';
    			}
    			str+='</div>';
    			$("#titles").append(str);
    		}
    	},'json');
    }
 function loadChildTitles(obj){
		var titleId=$(obj).attr("titleId");
		var childs=$(".belong_"+titleId);
		if(childs.length!=0){
			changeChildsStatus(obj,"block");
			return;
		}
		$.ajax({
			url:'/api/v1/title/'+titleId+'.html',
			type:'get',
			success:function(data){
				if(data.code==1){
        			 var str='';
						str+='<ul id="child_'+titleId+'" style="display: block;">';           			 
        			 for(var i=0;i<data.data.length;i++){
        			 	var o=data.data[i];
        			 	str+='<li class="belong_'+titleId+'"><a  href="/title/detail/'+o.titleId+'.html">'+o.titleName+'</a></li>';
        			 }
        			 str+='</ul>';
        			 $(obj).append(str);
				}else{
				}
			},
			error:function(){
				alert('加载子菜单失败');
			}
		})
	}
 function changeChildsStatus(obj,status){
		var titleId=$(obj).attr("titleId");
		if(status=='block')
		{
			$("#child_"+titleId).show();
		}else{
			$("#child_"+titleId).hide();
		}
	}
 function indexLoadLatestArticles(){
		jQuery.get('/api/v1/article/latest.html',function(data,textStatus,jqXHR){
			if(data.code==1){
				var str='';
				for(var i=0;i<data.data.length;i++){
					var o=data.data[i];
					str+='<li><a href="/article/detail/'+o.articleId+'.html"><i></i>'+o.articleName+'</a><span>'+o.createDateStr+'</span> <span style="margin-right:300px;">作者:'+o.articleAuthor+'</span></li>';
				}
				$("#latest_articles").append(str);
			}else{
				alert('加载最新文章失败:'+data.msg);
			}
		},'json');
	}

 
 function indexLoadNews(pageSize,pageNum){
 	$("#article_news").empty();
	 var params={"type":"news","pageSize":pageSize,"pageNum":pageNum};
 	jQuery.get('/api/v1/articles.html',params,function(data,textStatus,jqXHR){
 		var pageVO=data.data;
 		if(data.code==1){
 			var str='';
 			for(var i=0;i<data.data.data.length;i++){
 				var o=data.data.data[i];
 				str+='<li><a href="/article/detail/'+o.articleId+'.html"><i></i><span>'+o.articleName+'</span></a></li>';
 			}
 			str+='总共有'+pageVO.totalCount+'条记录';
 			if(pageVO.pageNum<=1){
 				str+='<a href="javascript:void(0)">上一页</a>';
 			}else{
 				str+='<a href="javascript:void(0)" onclick="loadNews(8,'+(pageVO.pageNum-1)+')">上一页</a>';
 			}
 			if(pageVO.pageNum>=pageVO.maxPage){
 				str+='<a href="javascript:void(0)">下一页</a>';
 			}else{
 				str+='<a href="javascript:void(0)" onclick="loadNews(8,'+(pageVO.pageNum+1)+')">下一页</a>';
 			}
 			$("#article_news").append(str);
 		}else{
 			alert('加载新闻失败');
 		}
 	},'json')
 }