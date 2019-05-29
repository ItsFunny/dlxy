package com.dlxy.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2019-05-21 13:37
 */
public class FlowConnectionVO
{
    @JsonProperty(value = "IP")
    private String ip;
    @JsonProperty(value = "MAC")
    private String Mac;
    @JsonProperty(value = "Counts")
    private Long counts;

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getMac()
    {
        return Mac;
    }

    public void setMac(String mac)
    {
        Mac = mac;
    }

    @Override
    public String toString()
    {
        return "FlowConnectionVO{" +
                "ip='" + ip + '\'' +
                ", Mac='" + Mac + '\'' +
                ", counts=" + counts +
                '}';
    }

    public Long getCounts()
    {
        return counts;
    }

    public void setCounts(Long counts)
    {
        this.counts = counts;
    }
}
