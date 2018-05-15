/*
 * Copyright (c) 2018 ncrypted
 * All rights reserved
 */

package de.ncrypted.rsb.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author ncrypted
 */
public class Transaction {

    private Timestamp dateRecorded;
    private int sender;
    private int target;
    private long money;

    public Transaction(Timestamp date_recorded, int sender, int target, long money) {
        this.dateRecorded = date_recorded;
        this.sender = sender;
        this.target = target;
        this.money = money;
    }

    public Transaction(int sender, int target, long money) {
        this.dateRecorded = new Timestamp(System.currentTimeMillis());
        this.sender = sender;
        this.target = target;
        this.money = money;
    }

    public Timestamp getDateRecorded() {
        return dateRecorded;
    }

    public int getSender() {
        return sender;
    }

    public int getTarget() {
        return target;
    }

    public long getMoney() {
        return money;
    }

    public String getInfos() {
        String action = "Transfer";
        String sender = "";
        String target = "";
        if (getSender() == -1) {
            action = "Zinsen";
        } else if (getSender() == 0) {
            action = "Einzahlung";
        } else {
            sender = Utils.toUserId(getSender());
        }
        if (getTarget() == 0) {
            action = "Auszahlung";
        } else {
            target = Utils.toUserId(getTarget());
        }
        return action + " " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(getDateRecorded()) + " " + sender + " " +
                target + " " + getMoney() + "$";
    }
}
