function editSaveUpdate(){
		var d=$("#editForm").serialize();
		jQuery.post('/api/v1/article/update.html',d,function(data,textStatus,jqXHR){
			if(data.code==1){
				alert('sucess');
				window.location.reload();
			}else{
				alert(data.msg);
			}
		},'json');
		
	}


	var ids="";
	$("#batchDelButton").click(function batchDel(){
		$("input:checkbox[name=checked]:checked").each(function(){
			ids+=$(this).val()+",";
		})
		var params={"ids":ids};
		jQuery.post('/api/article/del/batch.html',params,function(data,textStatus,jqXHR){
			if(data.code==1){
				alert('删除成功');
				window.location.href="http://localhost:8000/article/all.html";
			}else{
				alert('fail');
			}
		},'json');
	});
	function changeStatus(obj,status){
		var articleId=$(obj).attr("articleId");
		jQuery.get('/api/v1/article/update/status.html',{"articleId":articleId,"articleStatus":status},function(data,textStatus,jqXHR){
			if(data.code==1){
				alert('sucess');
				window.location.reload();
			}else{
				alert(data.msg);
			}
		},'json');		
	}
	$("#searchButton").click(function(){
		var q=$("#searchText").val();
		$("input[name='q']").val(q);
		$.ajax({
			type:'get',
			url:'/api/v1/search/article.html',
			data:$("#pageForm").serialize(),
			success:function(data,str){
				if(data.code==1){
					var pageVO=data.data;
					document.pageForm.q.value=q;
					var contentBody=$("#contentBody");
					contentBody.empty();
					for(var i=0;i<data.data.data.length;i++){
						var p=data.data.data[i];
						
						var t='<tr><td><input name="checked" lay-skin="primary" lay-filter="choose" type="checkbox" value=""><div class="layui-unselect layui-form-checkbox" lay-skin="primary"><i class="layui-icon"></i></div></td><td>'+p.articleId+'</td><td><a style="text-decoration:underline;" href="/admin/title/article/'+p.titleId+'.html">'+p.titleName+'</a></td><td align="left">'+p.articleName+'</td><td>'+p.articleAuthor+'</td>	<td> <a href="/admin/articles.html?userId='+p.userId+'" style="text-decoration:underline;" >'+p.username+'</a></td><td>'+p.createDateStr+'</td><td>'+p.updateDateStr+'</td>'
						if(p.status==0){
							t+='<td>下线</td><td><a class="layui-btn layui-btn-mini news_edit"  onclick="changeStatus(this,1);" articleId="'+p.articleId+'" "><i class="iconfont icon-edit" ></i> 上线</a><a class="layui-btn layui-btn-mini " data-toggle="modal" data-target="#changeSource" onclick="loadArticle(this);" articleId="'+p.articleId+'"><i class="iconfont icon-edit"></i> 编辑</a><a class="layui-btn layui-btn-mini "  onclick="changeStatus(this,3)" articleId="'+p.articleId+'"><i class="iconfont icon-edit"></i> 设为推荐新闻</a><a class="layui-btn layui-btn-danger layui-btn-mini news_del" data-id="1" articleId="'+p.articleId+'" onclick="changeStatus(this,2)" ><i class="layui-icon"></i> 放至回收站</a></td>';
						}else if(p.status==1){
							t+='<td>上线</td><td><a class="layui-btn layui-btn-mini news_edit"  articleId="'+p.articleId+'" onclick="changeStatus(this,0);"><i class="iconfont icon-edit" ></i> 下线</a><a class="layui-btn layui-btn-mini " data-toggle="modal" data-target="#changeSource" onclick="loadArticle(this);" articleId="'+p.articleId+'"><i class="iconfont icon-edit"></i> 编辑</a><a class="layui-btn layui-btn-mini "  onclick="changeStatus(this,3);" articleId="'+p.articleId+'"><i class="iconfont icon-edit"></i> 设为推荐新闻</a><a class="layui-btn layui-btn-danger layui-btn-mini news_del" data-id="1" articleId="'+p.articleId+'" onclick="changeStatus(this,2)" ><i class="layui-icon"></i> 放至回收站</a></td>';
						}else if(p.status==2){
							t+='<td>回收站</td><td><a class="layui-btn layui-btn-mini news_edit" articleId="'+p.articleId+'" onclick="changeStatus(this,0)"><i class="iconfont icon-edit" ></i> 恢复</a><a class="layui-btn layui-btn-mini " data-toggle="modal" data-target="#changeSource" onclick="loadArticle(this);" articleId="'+p.articleId+'"><i class="iconfont icon-edit"></i> 编辑</a><a class="layui-btn layui-btn-mini "  onclick="changeStatus(this,3);" articleId="'+p.articleId+'"><i class="iconfont icon-edit"></i> 设为推荐新闻</a><a class="layui-btn layui-btn-danger layui-btn-mini news_del" data-id="1" articleId="'+p.articleId+'" onclick="changeStatus(this,2)" ><i class="layui-icon"></i> 放至回收站</a></td>';
						}else{
							t+='<td>被推荐文章</td><td><a class="layui-btn layui-btn-mini news_edit" articleId="'+p.articleId+'" onclick="changeStatus(this,1)"><i class="iconfont icon-edit" ></i> 取消推荐</a><a class="layui-btn layui-btn-mini " data-toggle="modal" data-target="#changeSource" onclick="loadArticle(this);" articleId="'+p.articleId+'"><i class="iconfont icon-edit"></i> 编辑</a><a class="layui-btn layui-btn-mini "  onclick="changeStatus(this,3);" articleId="'+p.articleId+'"><i class="iconfont icon-edit"></i> 设为推荐新闻</a><a class="layui-btn layui-btn-danger layui-btn-mini news_del" data-id="1" articleId="'+p.articleId+'" onclick="changeStatus(this,2)" ><i class="layui-icon"></i> 放至回收站</a></td>';
						}
						contentBody.append(t);
					}
					
					var o=$("#layui-laypage-0");
					if(data.data.data.length==0){
						o.empty();
						o.append('<span class="layui-laypage-curr"><em class="layui-laypage-em"></em><em id="pageEm">暂无数据</em></span>')
					}else{
						o.empty();
						var s='<span class="layui-laypage-curr"><em class="layui-laypage-em"></em><em id="pageEm">当前第'+pageVO.pageNum+'页 总共有'+pageVO.maxPage+'页</em></span>';
						o.append(s);
						s="";
						if(pageVO.pageNum==1)
						{
							s+='<a href="javascript:void(0)">首页</a>';						
						}else{
							s+='<a href="javascript:void(0)" onclick="document.pageForm.pageNum.value=1;document.pageForm.submit();">首页</a>';
						}
						if(pageVO.pageNum<=1){
							s+=' <a href="javascript:void(0)">上一页</a>';
						}else{
							s+='<a href="javascript:void(0)" onclick="document.pageForm.pageNum.value=('+pageVO.pageNum-1+');document.pageForm.submit();"> 上一页</a>';
						}
						if(pageVO.pageNum>=pageVO.maxPage){
							s+='<a href="javascript:void(0)">下一页</a>';
							s+='<a href="javascript:void(0)">最后一页</a>';
						}else{
							s+='<a href="javascript:void(0)" onclick="document.pageForm.pageNum.value='+(pageVO.pageNum+1)+';document.pageForm.submit();">下一页</a> ';
							s+='<a href="javascript:void(0)" onclick="document.pageForm.pageNum.value='+pageVO.maxPage+';document.pageForm.submit();">最后一页</a>';
						}
						o.append(s);
					}
				}
			},
			error:function(data){
				alert('fail');
			}
		});
	});
	function singleDelButton(obj){
		var articleId=$(obj).attr("articleId");
		var params={"ids":articleId};
		jQuery.post('/api/v1/article/del/batch.html',params,function(data,textStatus,jqXHR){
			if(data.code==1){
				alert('删除成功');
				location.reload();
			}else{
				alert('fail');
			}
		},'json');
	}
	
		
	
	
	function loadArticle(obj){
		var articleId=$(obj).attr("articleId");
		alert(articleId);
		$("#edit_articleId").val(articleId);
		initkindEditor();
		jQuery.get('/api/v1/article/detail/'+articleId+'.html',function(data,textStatus,jqXHR){
			if(data.code==1){
				var article=data.data;
				$("#edit_articleName").val(article.articleName);
				$("#edit_articleAuthor").val(article.articleAuthor);
				KindEditor.html("#edit_articleContent", article.articleContent);
				var titleId=article.titleId;
				var parentTitleId=article.titleParentId;
				loadAllTitles(titleId,parentTitleId);
			}else{
				alert(data.msg);
			}
		},'json')
	}

    $("#leimu").change(function () {
        $("#leimu2").empty();
        var parentId=$("#leimu0").val();
            $.ajax({
                url:'/api/v1/title/'+parentId+'.html',
                type:'post',
                dataType:'json',
                success:function (data) {
					var son2=data.data;
                    var str="";
                    for(i=0;i<son2.length;i++){
                        str+="<option>"+son2[i].titleName+"</option>";
                    }
                    $("#leimu2").append(str);
                }
            })
	})	
 	function loadAllTitles(titleId,parentTitleId){
 	$("#leimu0").empty();
 	$("#leimu1").empty();
 		$.ajax({
 			url: '/api/v1/titles.html',
 			type:'get',
 			dataType:'json',
 			success:function(data){
 				var obj=data.data;
	 			var str="";
				for(i=0;i<obj.length;i++){
					var t=obj[i].titleId;
					if(t==parentTitleId){
					 str+='<option value="'+obj[i].titleId+'" selected="selected" ">'+obj[i].titleName+'</option>';
					}else{
					 str+='<option value="'+obj[i].titleId+'">'+obj[i].titleName+'</option>';
					}
				}
				$("#leimu0").append(str);
				showChild($("#leimu0"));
 			}
 		})
 	}
 	function showChild(obj){
 	 $("#leimu1").empty();
 		var titleId=$("#edit_articleId").val();
        var parentId=$(obj).val();
            $.ajax({
                url:'/api/v1/title/'+parentId+'.html',
                type:'post',
                dataType:'json',
                success:function (data) {
					var o=data.data;
                    var str="";
                    if(o.length<1){
                    	 $("#leimu1").remove();
                    }else{
                     $("#leimu1").remove();
                    	str+='<select id="leimu1" style="width:100px;height: 30px" name="titleId"> ';
                     for(i=0;i<o.length;i++){
                    	var t=o[i].titleId;
                    	if(t==titleId)
                    	{
                    		str+='<option value="'+o[i].titleId+'" selected="selected">'+o[i].titleName+'</option>';
                    	}else{
	                    	str+="<option value="+o[i].titleId+">"+o[i].titleName+"</option>"
                    	}
                    }
                    str+='</select>';
                    $("#leimu1_div").append(str);
                    }
                }
            })
 	}
 
 	function singleDelIndeed(obj){
 		var articleId=$(obj).attr("articleId");
 		jQuery.get('/api/v1/article/delete.html',{"articleId":articleId},function(data,textStatus,jqXHR){
 			alert(data.msg);
 		},'json');
 	}
 	