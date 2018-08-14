package com.dlxy.system.batch.service;

import java.io.File;
import java.net.URLEncoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dlxy.common.dto.ResultDTO;
import com.dlxy.common.utils.RSAUtils;
import com.dlxy.system.batch.config.DlxyProperty;
import com.joker.library.file.AbstractFIleStrategy;
import com.joker.library.file.IFileStrategy;

public class DefaultFileStrategyHandler extends AbstractDeleteFileHandler
{

	private Logger logger=LoggerFactory.getLogger(DefaultFileStrategyHandler.class);
	
	
	
	public DefaultFileStrategyHandler(String type)
	{
		super(type);
	}

	@Override
	protected boolean handler(DeleteHandlerObject obj)
	{
		String baseStoreUrl=super.context.getStoreBasePath(IFileStrategy.IMG_TYPE);
		String visitPrefix=super.context.getVisitPrefix(IFileStrategy.IMG_TYPE);
		if (StringUtils.isEmpty(baseStoreUrl))
		{
			baseStoreUrl = getStoreUrl();
			IFileStrategy fileStrategy = super.context.getFileStrategy();
			if(fileStrategy instanceof AbstractFIleStrategy)
			{
				((AbstractFIleStrategy)fileStrategy).getBasePathMap().put(IFileStrategy.IMG_TYPE, baseStoreUrl);
			}
		}
//		if(StringUtils.isEmpty(visitPrefix))
//		{
//			throw new RuntimeException("地址访问前缀必须配置");
//		}
		String fileUrl = baseStoreUrl +File.separator+ obj.getDbUrl().substring(obj.getDbUrl().indexOf(visitPrefix));
		return super.context.delete(fileUrl);
	}
	private String getStoreUrl()
	{
		String baseStoreUrl=null;
		try
		{
			String token = RSAUtils.encryptByPublic("getImgsAddress", getDlxyProperty().getPublicKeyBytes());
			@SuppressWarnings("rawtypes")
			ResultDTO resultDTO = getRestTemplate().getForObject(
					"http://localhost:8000/api/v1/address/images.html?token=" + URLEncoder.encode(token, "utf-8"),
					ResultDTO.class);
			if (resultDTO.getCode() == 1)
			{
				baseStoreUrl = (String) resultDTO.getData();
				System.out.println(baseStoreUrl);
				return baseStoreUrl;
			} else
			{
				logger.error("[rest请求存放图片地址出错]error:{}", resultDTO.getMsg());
				return null;
			}
		} catch (Exception e)
		{
			logger.error("[获取图片存储地址错误]error:{}", e.getMessage());
			baseStoreUrl = "/Users/joker/Java/oxygen_workspace/dlxy/dlxy-system/src/main/webapp";
			return baseStoreUrl;
		}
	}

	@Autowired
	@Override
	public void setDlxyProperty(DlxyProperty dlxyProperty)
	{
		// TODO Auto-generated method stub
		super.setDlxyProperty(dlxyProperty);
	}
	

}
