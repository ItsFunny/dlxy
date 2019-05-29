package com.dlxy.vo;

import java.util.Arrays;
import java.util.List;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2019-05-21 13:37
 */
public class FlowVO
{
    private List<FlowConnectionVO> connections;

    public List<FlowConnectionVO> getCurrentConnections()
    {
        return currentConnections;
    }

    public void setCurrentConnections(List<FlowConnectionVO> currentConnections)
    {
        this.currentConnections = currentConnections;
    }

    private List<FlowConnectionVO> currentConnections;


    private FlownDownloadVO flownDownloadVO;

    @Override
    public String toString()
    {
        return "FlowVO{" +
                "connections=" + connections +
                ", currentConnections=" + currentConnections +
                ", flownDownloadVO=" + flownDownloadVO +
                '}';
    }

    public List<FlowConnectionVO> getConnections()
    {
        return connections;
    }

    public void setConnections(List<FlowConnectionVO> connections)
    {
        this.connections = connections;
    }

    public FlownDownloadVO getFlownDownloadVO()
    {
        return flownDownloadVO;
    }

    public void setFlownDownloadVO(FlownDownloadVO flownDownloadVO)
    {
        this.flownDownloadVO = flownDownloadVO;
    }
}
