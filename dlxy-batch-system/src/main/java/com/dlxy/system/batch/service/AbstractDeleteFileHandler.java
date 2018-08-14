package com.dlxy.system.batch.service;



import org.springframework.web.client.RestTemplate;

import com.dlxy.system.batch.config.DlxyProperty;
import com.joker.library.file.FileStrategyContext;
import com.joker.library.file.IFileStrategy;

public abstract class AbstractDeleteFileHandler implements IFileDeleteHandler
{
	
	private String type;
	
	protected FileStrategyContext context;
	
	private IFileDeleteHandler nextHandler;
	
	private RestTemplate restTemplate;
	
	private DlxyProperty dlxyProperty;
	
	
	public String getBaseStorePath(String key)
	{
		return this.context.getStoreBasePath(key);
	}
	public String getVisitPrefix(String key) 
	{
		return this.context.getVisitPrefix(key);
	}
	
	public void setFileStrategy(IFileStrategy fileStrategy)
	{
		this.context.setFileStrategy(fileStrategy);
	}
	
	
	public AbstractDeleteFileHandler(String type)
	{
		this.type=type;
		this.context=new FileStrategyContext();
		this.restTemplate=new RestTemplate();
	}
	
	protected abstract boolean handler(DeleteHandlerObject obj);
	
	
	
	@Override
	public Boolean delete(DeleteHandlerObject obj)
	{
		if(obj.getType().equals(type))
		{
			return this.handler(obj);
		}else {
			if(this.nextHandler!=null)
			{
				return this.nextHandler.delete(obj);
			}else {
				throw new RuntimeException("没有找到符合的处理机制");
			}
		}
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}


	public IFileDeleteHandler getNextHandler()
	{
		return nextHandler;
	}

	public void setNextHandler(IFileDeleteHandler nextHandler)
	{
		this.nextHandler = nextHandler;
	}

	public RestTemplate getRestTemplate()
	{
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate)
	{
		this.restTemplate = restTemplate;
	}
	public DlxyProperty getDlxyProperty()
	{
		return dlxyProperty;
	}

	public void setDlxyProperty(DlxyProperty dlxyProperty)
	{
		this.dlxyProperty = dlxyProperty;
	}
	
}
