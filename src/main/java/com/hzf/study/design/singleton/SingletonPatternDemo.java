package com.hzf.study.design.singleton;

import org.joda.time.DateTime;

/**
 * @Author zhuofan.han
 * @Date 2020/11/23 14:12
 */
public class SingletonPatternDemo {
    public static void main(String[] args) {

        //不合法的构造函数
        //编译时错误：构造函数 SingleObject() 是不可见的
        //SingleObject object = new SingleObject();

        //获取唯一可用的对象
        SingleObject object = SingleObject.getInstance();

        //显示消息
        object.showMessage();

        Singleton.INSTANCE.showMessage();

        DateTime nowTime = DateTime.now();
        DateTime startOfDay = nowTime.withTimeAtStartOfDay();
        System.out.println(nowTime);
        System.out.println(startOfDay);
    }
}
