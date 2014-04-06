package com.cisco.dto;

import java.sql.Date;

/**
 * Created by Alf on 05.04.14.
 */
public class PrePos {

    private String TYPE;
    private int STA;
    private String PARTNER_NAME;
    private String PN;
    private double POS_SUM;
    private int QTY;
    private boolean GOOD;
    private int DELTA;
    private int SALE_Dis;
    private int BUY_Dis;
    private double SALE_Price;
    private double BUY_Price;
    private String PROMO1;
    private String PROMO2;
    private String END_USER;
    private String P_NUM;
    private Date INV_DATE;
    private String INV_NUMBER;
    private String SALES;
    private String Comment;
    private String SERIALS;
    private int ZIP;

    public PrePos(String TYPE, int STA, String PARTNER_NAME, Date INV_DATE) {
        this.TYPE = TYPE;
        this.STA = STA;
        this.PARTNER_NAME = PARTNER_NAME;
        this.INV_DATE = INV_DATE;
    }

    public PrePos(String TYPE, int STA, String PARTNER_NAME, String PN, double POS_SUM, int QTY, boolean GOOD, int DELTA, int SALE_Dis, int BUY_Dis, double SALE_Price, double BUY_Price, String PROMO1, String PROMO2, String END_USER, String p_NUM, Date INV_DATE, String INV_NUMBER, String SALES, String comment, String SERIALS, int ZIP) {

        this.TYPE = TYPE;
        this.STA = STA;
        this.PARTNER_NAME = PARTNER_NAME;
        this.PN = PN;
        this.POS_SUM = POS_SUM;
        this.QTY = QTY;
        this.GOOD = GOOD;
        this.DELTA = DELTA;
        this.SALE_Dis = SALE_Dis;
        this.BUY_Dis = BUY_Dis;
        this.SALE_Price = SALE_Price;
        this.BUY_Price = BUY_Price;
        this.PROMO1 = PROMO1;
        this.PROMO2 = PROMO2;
        this.END_USER = END_USER;
        P_NUM = p_NUM;
        this.INV_DATE = INV_DATE;
        this.INV_NUMBER = INV_NUMBER;
        this.SALES = SALES;
        Comment = comment;
        this.SERIALS = SERIALS;
        this.ZIP = ZIP;
    }

    public String getSALES() {
        return SALES;
    }

    public void setSALES(String SALES) {
        this.SALES = SALES;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public int getSTA() {
        return STA;
    }

    public void setSTA(int STA) {
        this.STA = STA;
    }

    public String getPARTNER_NAME() {
        return PARTNER_NAME;
    }

    public void setPARTNER_NAME(String PARTNER_NAME) {
        this.PARTNER_NAME = PARTNER_NAME;
    }

    public String getPN() {
        return PN;
    }

    public void setPN(String PN) {
        this.PN = PN;
    }

    public double getPOS_SUM() {
        return POS_SUM;
    }

    public void setPOS_SUM(double POS_SUM) {
        this.POS_SUM = POS_SUM;
    }

    public int getQTY() {
        return QTY;
    }

    public void setQTY(int QTY) {
        this.QTY = QTY;
    }

    public boolean isGOOD() {
        return GOOD;
    }

    public void setGOOD(boolean GOOD) {
        this.GOOD = GOOD;
    }

    public int getDELTA() {
        return DELTA;
    }

    public void setDELTA(int DELTA) {
        this.DELTA = DELTA;
    }

    public int getSALE_Dis() {
        return SALE_Dis;
    }

    public void setSALE_Dis(int SALE_Dis) {
        this.SALE_Dis = SALE_Dis;
    }

    public int getBUY_Dis() {
        return BUY_Dis;
    }

    public void setBUY_Dis(int BUY_Dis) {
        this.BUY_Dis = BUY_Dis;
    }

    public double getSALE_Price() {
        return SALE_Price;
    }

    public void setSALE_Price(double SALE_Price) {
        this.SALE_Price = SALE_Price;
    }

    public double getBUY_Price() {
        return BUY_Price;
    }

    public void setBUY_Price(double BUY_Price) {
        this.BUY_Price = BUY_Price;
    }

    public String getPROMO1() {
        return PROMO1;
    }

    public void setPROMO1(String PROMO1) {
        this.PROMO1 = PROMO1;
    }

    public String getPROMO2() {
        return PROMO2;
    }

    public void setPROMO2(String PROMO2) {
        this.PROMO2 = PROMO2;
    }

    public String getEND_USER() {
        return END_USER;
    }

    public void setEND_USER(String END_USER) {
        this.END_USER = END_USER;
    }

    public String getP_NUM() {
        return P_NUM;
    }

    public void setP_NUM(String p_NUM) {
        P_NUM = p_NUM;
    }

    public Date getINV_DATE() {
        return INV_DATE;
    }

    public void setINV_DATE(Date INV_DATE) {
        this.INV_DATE = INV_DATE;
    }

    public String getINV_NUMBER() {
        return INV_NUMBER;
    }

    public void setINV_NUMBER(String INV_NUMBER) {
        this.INV_NUMBER = INV_NUMBER;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getSERIALS() {
        return SERIALS;
    }

    public void setSERIALS(String SERIALS) {
        this.SERIALS = SERIALS;
    }

    public int getZIP() {
        return ZIP;
    }

    public void setZIP(int ZIP) {
        this.ZIP = ZIP;
    }
}
