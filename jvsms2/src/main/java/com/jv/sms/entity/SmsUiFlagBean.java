package com.jv.sms.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8.
 */

public class SmsUiFlagBean {

    public List<Boolean> hasIconUi = new ArrayList<>();

    public SmsUiFlagBean() {
    }

    public SmsUiFlagBean(int size) {
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
        for (int i = 0; i < hasIconUi.size(); i++) {
            if (hasIconUi.contains(false)) {
                return true;
            }
        }
        return false;
    }

    public void initHasMessageUi() {
        for (int i = 0; i < hasIconUi.size(); i++) {
            hasIconUi.set(i, true);
        }
    }

    public void setFlag(int position, boolean flag) {
        if (!flag) {
            flag = hasIconUi.get(position) == true ? flag : true;
            hasIconUi.set(position, flag);
        } else {
            hasIconUi.set(position, flag);
        }
    }

    public boolean selectFlag() {
        for (int i = 0; i < hasIconUi.size(); i++) {
            if (hasIconUi.get(i) == false) {
                return false;
            }
        }
        return true;
    }

    public int[] getSelectPosition() {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < hasIconUi.size(); i++) {
            if (hasIconUi.get(i) == false) {
                positions.add(i);
            }
        }
        int[] positionArray = new int[positions.size()];
        int index = 0;
        for (int i = (positions.size() - 1); i >= 0; i--) {
            positionArray[index] = positions.get(i);
            index++;
        }
        return positionArray;
    }

}
