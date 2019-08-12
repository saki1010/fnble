package com.fengniao.fnbluetooth.bluetooth.recivier.observer;

/**
 * Created by kim on 15/01/2018.
 * 被观察者
 */

public interface Subject {

    /**
     * add Observer instance
     * @param observer
     */
    public void attach(MyObserver observer);


    /**
     * detach Observer
     * @param observer
     */
    public void detach(MyObserver observer);


    /**
     *
     * @param message
     */
    public void notify(String message, byte[] data);

}
