package com.dlxy.vo;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2019-05-21 13:37
 */
public class FlownDownloadVO
{
    private Long recordTime;

    public Long getRecordTime()
    {
        return recordTime;
    }

    @Override
    public String toString()
    {
        return "FlownDownloadVO{" +
                "recordTime=" + recordTime +
                ", downBps=" + downBps +
                ", upBps=" + upBps +
                ", peekDownBps=" + peekDownBps +
                ", peekUpStreamBps=" + peekUpStreamBps +
                '}';
    }

    public void setRecordTime(Long recordTime)
    {
        this.recordTime = recordTime;
    }

    public Float getDownBps()
    {
        return downBps;
    }

    public void setDownBps(Float downBps)
    {
        this.downBps = downBps;
    }

    public Float getUpBps()
    {
        return upBps;
    }

    public void setUpBps(Float upBps)
    {
        this.upBps = upBps;
    }

    public Float getPeekDownBps()
    {
        return peekDownBps;
    }

    public void setPeekDownBps(Float peekDownBps)
    {
        this.peekDownBps = peekDownBps;
    }

    public Float getPeekUpStreamBps()
    {
        return peekUpStreamBps;
    }

    public void setPeekUpStreamBps(Float peekUpStreamBps)
    {
        this.peekUpStreamBps = peekUpStreamBps;
    }

    private Float downBps;
    private Float upBps;
    private Float peekDownBps;
    private Float peekUpStreamBps;

}
