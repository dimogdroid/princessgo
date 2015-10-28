/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.difed.service.model;

public class Result {
    
    private User user;
    private Ranking ranking;
    private UserRanking userRanking;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    public UserRanking getUserRanking() {
        return userRanking;
    }

    public void setUserRanking(UserRanking userRanking) {
        this.userRanking = userRanking;
    }
    
}
