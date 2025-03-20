package com.sultan.goldquiz;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class WithdrawRequest {

    private String userId;
    private String emailAddress;
    private String accountTitle;
    private String accountNumber;
    private String requestedBy;
    private long coins;

    @ServerTimestamp
    private Date createdAt;

    public WithdrawRequest() {

    }

    public WithdrawRequest(String userId, String emailAddress, String accountTitle, String accountNumber, String requestedBy,long coins) {
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.accountTitle = accountTitle;
        this.accountNumber = accountNumber;
        this.requestedBy = requestedBy;
        this.coins = coins;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }
}
