///**
//*
//* @author joker 
//* @date 创建时间：2018年8月5日 上午8:04:27
//* 
//*/
//package com.dlxy.controller;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.shiro.authz.annotation.RequiresRoles;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.dlxy.common.dto.ArticleDTO;
//import com.dlxy.common.dto.DlxyTitleDTO;
//import com.dlxy.common.enums.ArticleStatusEnum;
//import com.dlxy.common.enums.DlxyTitleEnum;
//import com.dlxy.common.service.IdWorkerService;
//import com.dlxy.server.article.service.ITitleService;
//import com.dlxy.service.IRedisService;
//import com.dlxy.service.command.AddOrUpdateArtilceCommand;
//
///**
// * 
// * @author joker
// * @date 创建时间：2018年8月5日 上午8:04:27
// */
//@Controller
//@RequestMapping("/test")
//public class TestController
//{
//	@Autowired
//	private IRedisService redisService;
//	private Logger logger = LoggerFactory.getLogger(TestController.class);
//
//	@RequiresRoles("admin")
//	@RequestMapping("/t")
//	@ResponseBody
//	public String t()
//	{
//		return "oK";
//	}
//
//	@ResponseBody
//	@RequestMapping("/redis")
//	public String testRedis()
//	{
//		try
//		{
//			String json = redisService.get("USER_VISIT_COUNTS:0.0.0.0.0.0.0.1");
//			return json;
//		} catch (Exception e)
//		{
//			return "redis挂了" + e.getMessage();
//		}
//
//	}
//
//	@RequestMapping("/date")
//	public ModelAndView testDate()
//	{
//
//		return new ModelAndView("date");
//	}
//
//	@RequestMapping("/doDate")
//	public void testDate(HttpServletRequest request, HttpServletResponse response)
//	{
//		System.out.println(request.getParameter("startTime"));
//	}
//
//	@RequestMapping("/log")
//	@ResponseBody
//	public void logTest()
//	{
//		logger.debug("test_debug");
//		logger.info("test_info");
//		logger.warn("test_warn");
//		logger.error("test_error");
//	}
//
//	@Autowired
//	private AddOrUpdateArtilceCommand articleCommand;
//
//	@Autowired
//	private ITitleService titleService;
//	@Autowired
//	private IdWorkerService idService;
//
//	@RequestMapping("/zrem/{articleId}")
//	public void testZrem(@PathVariable("articleId") String articleId)
//	{
//		List<Long> articleidList = new ArrayList<>();
//		articleidList.add(393085177452167168L);
//		articleidList.add(393085176797855744L);
//		List<String> vList = new ArrayList<>();
//		vList.add("as");
//		vList.add("ss");
//		String[] array = vList.toArray(new String[vList.size()]);
//		redisService.zRem(IRedisService.ARTICLE_VISIT_RANGE, array);
//
//	}
//
//	public static void main(String[] args)
//	{
//		List<String> vList = new ArrayList<>();
//		vList.add("as");
//		vList.add("ss");
//		String[] array = vList.toArray(new String[vList.size()]);
//		for (String string : array)
//		{
//			System.out.println(string);
//			
//		}
//
//	}
//
//	@RequestMapping("/init")
//	@ResponseBody
//	public String initDb()
//	{
//		String articleContent = "<p style=\"text-align:center;\">\n"
//				+ "	<span style=\"color:#333333;font-family:&quot;font-size:14px;\">&nbsp; &nbsp;2018年3月13日星期二，吉林省教育厅党组书记、厅长，省高校工委书记李晓杰，省政府教育督导委员会总督学许世彬一行到吉林农业科技学院-鼎利学院进行考察调研。陪同的有：校党委书记徐大林，校长张立峰，鼎利学院院校领导。</span>\n"
//				+ "</p>\n" + "<p style=\"text-align:center;\">\n"
//				+ "	<img src=\"http://120.78.240.211/imgs/393036493431635968/3da8c95c-7d65-4038-bd11-f6fc2c209130.jpg?pictureId=172\" alt=\"\" />\n"
//				+ "</p>\n" + "<p style=\"text-align:center;\">\n" + "	<br />\n" + "</p>\n"
//				+ "<p style=\"text-align:center;\">\n"
//				+ "	<p style=\"color:#333333;font-family:&quot;font-size:14px;\">\n"
//				+ "		&nbsp;徐大林代表学校对李晓杰一行的到来表示热烈欢迎，向教育厅长期以来对学校的指导和关心表示衷心感谢。他表示，近年来在省教育厅、省高校工委的大力支持下，学校各方面工作取得了长足发展，但在发展中也面临着一些困难和问题，望省教育厅进一步加大对学校的支持力度，学校也将积极采取措施，坚持立德树人，全面深化改革，进一步推进转型发展和内涵建设，加快特色鲜明的应用型大学建设步伐，为吉林省经济社会发展做出更大的贡献。\n"
//				+ "	</p>\n" + "<br />\n"
//				+ "<img src=\"http://120.78.240.211/imgs/393036493431635968/3f65e83a-c15c-46ca-8167-31cee7b8dfaa.jpg?pictureId=173\" alt=\"\" />\n"
//				+ "</p>\n" + "<p style=\"text-align:center;\">\n"
//				+ "	<span style=\"color:#333333;font-family:&quot;font-size:14px;\">在学校领导的陪同下，李晓杰一行来到鼎利学院。在走访鼎利学院实训室时，李晓杰与企业工程师共同探讨产教融合、校企合作及鼎利教育UBL人才培养模式。她指出，吉林农业科技学院是全省首批应用型转型发展试点高校，经过长期实践，鼎利学院应运而生，使得学校的转型发展目标和办学理念更加清晰。李晓杰对我校应用技能型人才培养取得的成绩给予充分肯定，希望学校继续结合地方经济社会发展的需要，推进产教融合，加快独有的省属特色大学、特色学科“双特色”建设，科学定位，发挥优势，为吉林省新一轮全面振兴发展提供强有力的人才保障和智力支持。</span>\n"
//				+ "</p>";
//		String articleAuthor = "joker";
//		String[] articleNames = new String[]
//		{ "吉林省到鼎利学院考察调研", "世纪鼎利“新工科下的产业学院", "世纪鼎利出席国家级新工科研究与实践项目启动会暨人工智能高峰论坛筹备会",
//				"西安欧亚学院教师发展中心主任戚世梁等一行考察电气与信息工程学院和鼎利学院", "电气与信息工程学院召开教师培训交流大会", "电气与信息工程学院携手光明社区举办邻里情百家宴活动 ",
//				"关于吉林市第八次社会科学优秀成果奖评审工作的通知", "西安欧亚学院教师发展中心", "学院召开教师培", "家级新工科研究与实践项目", "教育厅" };
//		String[] shortNames = new String[]
//		{ "教育厅", "动力", "科技", "鼎力", "学校", "学院", "初始", "放弃", "无奈", "成就", "目标", "结果", "信息", "国家" };
//		int length = articleNames.length;
//		long startTime = System.currentTimeMillis();
//		System.out.println("begin");
//		List<DlxyTitleDTO> news = titleService.findTitlesByType(DlxyTitleEnum.NEWS_TITLE.ordinal());
//		List<DlxyTitleDTO> upTiltes = titleService.findTitlesByType(DlxyTitleEnum.UP_TITLE.ordinal());
//		news.addAll(upTiltes);
//		List<Integer> titleIdList = new ArrayList<Integer>();
//		news.forEach(n -> titleIdList.add(n.getTitleId()));
//		// upTiltes.forEach(u -> titleIdList.add(u.getTitleId()));
//		List<Integer> childIdList = new ArrayList<>();
//		for (Integer integer : titleIdList)
//		{
//			List<DlxyTitleDTO> childs = titleService.findChildsByParentId(integer);
//			if (childs != null && !childs.isEmpty())
//			{
//				for (DlxyTitleDTO dlxyTitleDTO : childs)
//				{
//					childIdList.add(dlxyTitleDTO.getTitleId());
//				}
//			}
//		}
//		Random random = new Random();
//		titleIdList.addAll(childIdList);
//		ArticleDTO articleDTO = new ArticleDTO();
//		articleDTO.setArticleAuthor(articleAuthor);
//		articleDTO.setArticleContent(articleContent);
//		articleDTO.setUserId(1L);
//		articleDTO.setRealname("joker");
//		for (int i = 0; i < 50; i++)
//		{
//			for (Integer integer : titleIdList)
//			{
//				long articleId = idService.nextId();
//				int randInt = random.nextInt(length);
//				String temp = articleNames[randInt];
//				String articleName = "";
//				if (randInt < (length / 2))
//				{
//					articleAuthor = temp;
//				} else
//				{
//					char[] chars = temp.toCharArray();
//					Integer limit = random.nextInt(8);
//					for (int m = 0; m < limit; m++)
//					{
//						Random random2 = new Random();
//						articleName += chars[random2.nextInt(chars.length)];
//					}
//				}
//				articleDTO.setArticleName(articleName + shortNames[random.nextInt(shortNames.length)] + integer);
//				articleDTO.setTitleId(integer);
//				articleDTO.setArticleId(articleId);
//				articleDTO.setArticleStatus(ArticleStatusEnum.UP.ordinal());
//				Map<String, Object> params = new HashMap<String, Object>();
//				params.put("articleDTO", articleDTO);
//				articleCommand.execute(params);
//				try
//				{
//					Thread.sleep(200);
//				} catch (InterruptedException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		for (int i = 0; i < 300; i++)
//		{
//			long articleId = idService.nextId();
//			int randInt = random.nextInt(length);
//			String temp = articleNames[randInt];
//			String articleName = "";
//			if (randInt < (length / 2))
//			{
//				articleAuthor = temp;
//			} else
//			{
//				char[] chars = temp.toCharArray();
//				Integer limit = random.nextInt(8);
//				for (int m = 0; m < limit; m++)
//				{
//					Random random2 = new Random();
//					articleName += chars[random2.nextInt(chars.length)];
//				}
//			}
//			articleDTO.setArticleId(articleId);
//			articleDTO.setArticleName(articleName + shortNames[random.nextInt(shortNames.length)] + i);
//			articleDTO.setTitleId(titleIdList.get(random.nextInt(titleIdList.size())));
//			articleDTO.setArticleStatus(ArticleStatusEnum.UP.ordinal());
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("articleDTO", articleDTO);
//			articleCommand.execute(params);
//			try
//			{
//				Thread.sleep(150);
//			} catch (InterruptedException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		System.out.println("consume:" + (System.currentTimeMillis() - startTime));
//		return "ok";
//	}
//
//}
