package cn.com.startai.qx.utils;

/**
 * Created by Robin on 2019/3/8.
 * 419109715@qq.com 彬影
 */
public class EventBean {


    private String action;
    private Object object1;
    private Object object2;
    private Object object3;
    private Object object4;


    public EventBean(String action) {
        this.action = action;
    }

    public EventBean(String action, Object object1) {
        this.action = action;
        this.object1 = object1;
    }

    public EventBean(String action, Object object1, Object object2) {
        this.action = action;
        this.object1 = object1;
        this.object2 = object2;
    }

    public EventBean(String action, Object object1, Object object2, Object object3) {
        this.action = action;
        this.object1 = object1;
        this.object2 = object2;
        this.object3 = object3;
    }

    public EventBean(String action, Object object1, Object object2, Object object3, Object object4) {
        this.action = action;
        this.object1 = object1;
        this.object2 = object2;
        this.object3 = object3;
        this.object4 = object4;
    }

    @Override
    public String toString() {
        return "EventBean{" +
                "action='" + action + '\'' +
                ", object1=" + object1 +
                ", object2=" + object2 +
                ", object3=" + object3 +
                ", object4=" + object4 +
                '}';
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public Object getObject1() {
        return object1;
    }

    public void setObject1(Object object1) {
        this.object1 = object1;
    }

    public Object getObject2() {
        return object2;
    }

    public void setObject2(Object object2) {
        this.object2 = object2;
    }

    public Object getObject3() {
        return object3;
    }

    public void setObject3(Object object3) {
        this.object3 = object3;
    }

    public Object getObject4() {
        return object4;
    }

    public void setObject4(Object object4) {
        this.object4 = object4;
    }


}
