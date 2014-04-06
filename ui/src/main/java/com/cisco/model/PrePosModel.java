package com.cisco.model;

import java.sql.Date;

/**
 * Created by Alf on 05.04.14.
 */
public class PrePosModel {

    private String TYPE;
    private int STA;
    private String PARTNER_NAME;
    private Date INV_DATE;

    public PrePosModel() {
    }

    public PrePosModel(String TYPE, int STA, String PARTNER_NAME, Date INV_DATE) {
        this.TYPE = TYPE;
        this.STA = STA;
        this.PARTNER_NAME = PARTNER_NAME;
        this.INV_DATE = INV_DATE;
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

    public Date getINV_DATE() {
        return INV_DATE;
    }

    public void setINV_DATE(Date INV_DATE) {
        this.INV_DATE = INV_DATE;
    }
}
