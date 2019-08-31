package com.yyy.stapo.rework;

import java.util.List;

public class ReworkBean {

    /**
     * success : true
     * message :
     * tables : [[{"iRecNo":212151,"sCode":"柳树-奶白平板260","sName":"","sFlowerCode":"s139","sUserName":"老验布机","sBarcode":"00771934","fQty":50.7,"iFlawCount":5,"fFlawQty":0}]]
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

    public static class TablesBean {
        /**
         * iRecNo : 212151
         * sCode : 柳树-奶白平板260
         * sName :
         * sFlowerCode : s139
         * sUserName : 老验布机
         * sBarcode : 00771934
         * fQty : 50.7
         * iFlawCount : 5
         * fFlawQty : 0.0
         */

        private int iRecNo;
        private String sCode;
        private String sName;
        private String sFlowerCode;
        private String sUserName;
        private String sBarcode;
        private double fQty;
        private int iFlawCount;
        private float fFlawQty;

        public int getIRecNo() {
            return iRecNo;
        }

        public void setIRecNo(int iRecNo) {
            this.iRecNo = iRecNo;
        }

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

        public String getSFlowerCode() {
            return sFlowerCode;
        }

        public void setSFlowerCode(String sFlowerCode) {
            this.sFlowerCode = sFlowerCode;
        }

        public String getSUserName() {
            return sUserName;
        }

        public void setSUserName(String sUserName) {
            this.sUserName = sUserName;
        }

        public String getSBarcode() {
            return sBarcode;
        }

        public void setSBarcode(String sBarcode) {
            this.sBarcode = sBarcode;
        }

        public double getFQty() {
            return fQty;
        }

        public void setFQty(double fQty) {
            this.fQty = fQty;
        }

        public int getIFlawCount() {
            return iFlawCount;
        }

        public void setIFlawCount(int iFlawCount) {
            this.iFlawCount = iFlawCount;
        }

        public float getFFlawQty() {
            return fFlawQty;
        }

        public void setFFlawQty(float fFlawQty) {
            this.fFlawQty = fFlawQty;
        }
    }
}
