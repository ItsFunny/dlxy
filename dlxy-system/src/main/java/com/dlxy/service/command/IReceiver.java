package com.dlxy.service.command;

import java.sql.SQLException;
import java.util.Map;

public interface IReceiver
{
	void add(Map<String, Object>map) throws SQLException;
	
	void update(Map<String, Object>params) throws SQLException;
	
	
	void delete(Map<String, Object>params);
}
