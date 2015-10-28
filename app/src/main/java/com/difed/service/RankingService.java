/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.difed.service;

//import difed.christmas.service.model.Result;
//import difed.christmas.service.model.User;


import com.difed.service.model.Result;
import com.difed.service.model.User;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


public interface RankingService {
    
//    @FormUrlEncoded
//    @POST("/ranking")
//    Response listRanking(
//            @Field("level") int level, 
//            @Field("deviceId") String deviceId);
	
	
	@FormUrlEncoded
    @POST("/ranking")
    Result listRanking(
            @Field("level") int level,
            @Field("deviceId") String deviceId);
    
    
	 @FormUrlEncoded
	 @POST("/guarda")
     User postGuarda(
             @Field("level") int level,
             @Field("deviceId") String deviceId,
             @Field("time") int time,
             @Field("username") String username);
    
	        
    
    @FormUrlEncoded
    @POST("/time")
    User postTime(
            @Field("level") int level,
            @Field("deviceId") String deviceId);
//    
//    @FormUrlEncoded
//    @POST("/time")
//    Response postTime(
//            @Field("level") int level, 
//            @Field("deviceId") String deviceId, 
//            @Field("time") int time, 
//            @Field("username") String username);
//    
}
