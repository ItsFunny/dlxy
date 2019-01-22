package com.dlxy.model;

import java.lang.ref.SoftReference;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2019-01-22 20:26
 */
public class SoftReferenceTest
{
    public static class SoftReferenceClass
    {
        public String name;
        public Integer age;
    }


    public static void main(String[] args)
    {
        SoftReference<SoftReferenceClass> soft = new SoftReference<SoftReferenceClass>();
        SoftReferenceClass referenceClass = soft.get();
        System.out.println(referenceClass.age);
    }
}
