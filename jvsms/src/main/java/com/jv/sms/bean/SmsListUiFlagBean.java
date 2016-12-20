package com.jv.sms.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8.
 */

public class SmsListUiFlagBean {

    public List<Boolean> hasDateUi = new ArrayList<>();
    public List<Boolean> hasMessageUi = new ArrayList<>();

    public SmsListUiFlagBean() {
    }

    public SmsListUiFlagBean(int size) {
        for (int i = 0; i < size; i++) {
            hasDateUi.add(true);
            hasMessageUi.add(true);
        }
    }

    public void updateSize(int num) {
        for (int i = 0; i < num; i++) {
            hasDateUi.add(true);
            hasMessageUi.add(true);
        }
    }

    public boolean extendHasMessageUi() {
        for (int i = 0; i < hasMessageUi.size(); i++) {
            if (hasMessageUi.contains(false)) {
                return true;
            }
        }
        return false;
    }

    public int getSelectMessageUiPosition() {
        for (int i = 0; i < hasMessageUi.size(); i++) {
            if (!hasMessageUi.get(i)) {
                return i;
            }
        }
        return -1;
    }

    public void initHasMessageUi() {
        for (int i = 0; i < hasMessageUi.size(); i++) {
            hasMessageUi.set(i, true);
        }
    }

}
