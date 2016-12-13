package com.jv.sms.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8.
 */

public class SmsUiFlagBean {

    public List<Boolean> hasIconUi = new ArrayList<>();
    public int size;

    public SmsUiFlagBean() {
    }

    public SmsUiFlagBean(int size) {
        this.size = size;
        for (int i = 0; i < size; i++) {
            hasIconUi.add(true);
        }
    }

    public void updateSize(int num) {
        for (int i = 0; i < num; i++) {
            hasIconUi.add(true);
        }
    }

    public boolean extendHasMessageUi() {
        for (int i = 0; i < size; i++) {
            if (hasIconUi.contains(false)) {
                return true;
            }
        }
        return false;
    }

    public void initHasMessageUi() {
        for (int i = 0; i < size; i++) {
            hasIconUi.set(i, true);
        }
    }

}
