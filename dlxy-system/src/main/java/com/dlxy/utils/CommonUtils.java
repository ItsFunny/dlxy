package com.dlxy.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2019-05-22 15:39
 */
public class CommonUtils
{
    @SuppressWarnings("unchecked")
    public static <T> List<T> ParseJson2Type(String json, Class<T> type)
    {
        JSONArray jsonArray = JSONArray.fromObject(json);
        List<T> l = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++)
        {
            l.add((T) JSONObject.toBean(jsonArray.getJSONObject(i), type));
        }
        return l;
    }
}
