///**
//*
//* @Description
//* @author joker 
//* @date 创建时间：2018年7月31日 下午3:31:47
//* 
//*/
//package com.dlxy.service.command;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.dlxy.common.enums.PictureStatusEnum;
//import com.dlxy.utils.FileUtil;
//
///**
//* 
//* @When
//* @Description
//* @Detail
//* @author joker 
//* @date 创建时间：2018年7月31日 下午3:31:47
//*/
//public class DeleteArticleCommand extends Command
//{
//
//	private Logger logger=LoggerFactory.getLogger(DeleteArticleCommand.class);
//	@Override
//	public void execute(Map<String, Object> param)
//	{
//		Long[] articleIds=(Long[]) param.get("ids");
//		List<Long> ids = Arrays.asList(articleIds);
//		List<Long> backUpdateIds = new ArrayList<>();
//		List<Long>deleteIds=new ArrayList<>();
//		Map<String, Object>params=new HashMap<String, Object>();
//		param.put("articleIds", articleIds);
//		articleGroup.delete(params);
//		
//		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = attributes.getRequest();
//		String realPath = request.getServletContext().getRealPath("imgs");
//		for (int i = ids.size() - 1; i >= 0; i--)
//		{
//			File file = new File(realPath + File.separator + ids.get(i));
//			try
//			{
//				FileUtil.delFileOrDir(file);
//				deleteIds.add(ids.get(i));
//			} catch (Exception e)
//			{
//				logger.error("[删除文章旗下的图片失败],file:{}", file);
//				backUpdateIds.add(ids.get(i));
//			}
//		}
//		if(!deleteIds.isEmpty())
//		{
//			param.clear();
//			param.put("pictureIds", deleteIds);
//			pictureGroup.delete(params);
////			pictureService.deleteByPictureIds(deleteIds);
//		}
//		if (!backUpdateIds.isEmpty())
//		{
//			pictureService.updateArticlePictureStatusByArticleIdsInbatch(
//					backUpdateIds.toArray(new Long[backUpdateIds.size()]), PictureStatusEnum.Invalid.ordinal());
//		}
//	}
//
//}
