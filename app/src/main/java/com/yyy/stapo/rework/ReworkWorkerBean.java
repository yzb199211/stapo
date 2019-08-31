package com.yyy.stapo.rework;

import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

import java.util.List;

public class ReworkWorkerBean {

    /**
     * success : true
     * message :
     * tables : [[{"sCode":"B001","sName":"王菊琴"},{"sCode":"B002","sName":"汪小兰"},{"sCode":"B003","sName":"俞燕梅"},{"sCode":"B004","sName":"常家田"},{"sCode":"B005","sName":"梁珍"},{"sCode":"B006","sName":"王亚琴"}]]
     */

    private boolean success;
    private String message;
    private List<List<TablesBean>> tables;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<List<TablesBean>> getTables() {
        return tables;
    }

    public void setTables(List<List<TablesBean>> tables) {
        this.tables = tables;
    }

    public static class TablesBean implements IPickerViewData {
        /**
         * sCode : B001
         * sName : 王菊琴
         */

        private String sCode;
        private String sName;

        public String getSCode() {
            return sCode;
        }

        public void setSCode(String sCode) {
            this.sCode = sCode;
        }

        public String getSName() {
            return sName;
        }

        public void setSName(String sName) {
            this.sName = sName;
        }

        @Override
        public String getPickerViewText() {
            return sName;
        }
    }
}
