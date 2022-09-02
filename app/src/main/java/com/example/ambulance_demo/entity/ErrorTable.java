package com.example.ambulance_demo.entity;

public class ErrorTable {
    private int err_id;
    private String err_msg;
    private Boolean err_status;
    private String err_type;

    public ErrorTable(int err_id, String err_msg, Boolean err_status) {
        this.err_id = err_id;
        this.err_msg = err_msg;
        this.err_status = err_status;
    }

    public String getErr_type() {
        return err_type;
    }

    public void setErr_type(String err_type) {
        this.err_type = err_type;
    }

    public ErrorTable(int err_id, String err_msg, Boolean err_status, String err_type) {
        this.err_id = err_id;
        this.err_msg = err_msg;
        this.err_status = err_status;
        this.err_type = err_type;
    }

    public Boolean getErr_status() {
        return err_status;
    }

    public void setErr_status(Boolean err_status) {
        this.err_status = err_status;
    }

    public int getErr_id() {
        return err_id;
    }

    public void setErr_id(int err_id) {
        this.err_id = err_id;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }
}
