/**
*
* @Description
* @author joker 
* @date 创建时间：2018年7月29日 下午1:53:04
* 
*/
package com.dlxy.system.batch.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dlxy.system.batch.config.BatchSystemConfiguration;
import com.dlxy.system.batch.config.DlxyProperty;
import com.dlxy.system.batch.jobs.JobRunner;
import com.dlxy.system.batch.service.IRedisService;

/**
 * 
 * @When
 * @Description
 * @Detail
 * @author joker
 * @date 创建时间：2018年7月29日 下午1:53:04
 */
@RestController
@RequestMapping("/batch")
public class MonitorController
{
	@Autowired
	private IRedisService redisService;

	@Autowired
	private BatchSystemConfiguration configuration;
	private Logger logger = LoggerFactory.getLogger(MonitorController.class);

	@SuppressWarnings("unchecked")
	@RequestMapping("/monitor")
	public Map<String, Object> monitorBatchJob(@RequestParam(name = "class", required = false) String jobClass)
	{
		Map<String, Object> res = new HashMap<String, Object>();
		logger.info("  monitor batch {} ", jobClass);
		res.put("sucess", false);
		List<JobRunner> jobList = new ArrayList<>();

		String[] batchClasses = configuration.getProperties().getBatchClasses();

		if (StringUtils.isEmpty(jobClass))
		{
			for (int i = 0; i < batchClasses.length; i++)
			{
				try
				{
					JobRunner runner = configuration
							.createBean((Class<? extends JobRunner>) Class.forName(batchClasses[i]));
					jobList.add(runner);
				} catch (Exception e)
				{
					logger.error("cant find job name : {}", batchClasses[i], e);
				}
			}
		} else
		{
			for (int i = 0; i < batchClasses.length; i++)
			{
				if (StringUtils.indexOf(batchClasses[i], jobClass) >= 0)
				{
					try
					{
						JobRunner job = configuration
								.createBean((Class<? extends JobRunner>) Class.forName(batchClasses[i]));
						jobList.add(job);
					} catch (Exception e)
					{
						logger.error("cant find job name : {}", batchClasses[i], e);
					}

				}
			}
		}

		List<String> names = new ArrayList<>();
		logger.info("find jobs {}", jobList);
		for (JobRunner jobRunner : jobList)
		{
			Thread t = new Thread(jobRunner);
			t.start();
			names.add(jobRunner.toString());
		}
		res.put("success", true);
		res.put("jobs", names);
		return res;
	}
}
