package com.arits.docRepo.util;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;

import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.exception.DMSException;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;

import io.jsonwebtoken.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class JWTUtil {
	
	
	//As part of POC just hard coded the private key value instead of reading it from file  
	private static String SECRET_KEY = "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";
	
	private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256; 
    //Sample method to construct a JWT
    public static String createJWT(UserDetails userDetails) {
    	System.out.println("Login id "+ userDetails.getLoginId());
       
    	//The JWT signature algorithm which will be used to sign the token    	
              
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis+900000);
        //Date nbf = new Date(nowMillis+30000);
       
       
	
        
        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(apiKeySecretBytes);
       
        KeyFactory kf;
        PrivateKey privKey;
        JwtBuilder builder =null;
		try {
			kf = KeyFactory.getInstance("RSA");
			privKey = kf.generatePrivate(keySpec);
			builder = Jwts.builder()
					.setHeaderParam("alg", "RS256")
					.setHeaderParam("typ", "JWT")
					.setId("0B3EE6CBF68A2F7FCAAA472E11922015")
	                .setIssuedAt(now)
	                .setExpiration(exp)
	                .setSubject("DMSAPI")
	                .setAudience("DMSUI")
	                //.claim("pin", userDetails.getPassCode())
	                .claim("username", userDetails.getUserName())
	                .claim("usergroup", userDetails.getUserGroup())
	                .claim("loginid", userDetails.getLoginId())
	                .setIssuer("fd3b0648616674825338e8b1689e2407")
	                .signWith(signatureAlgorithm, privKey);
	        
		} catch (NoSuchAlgorithmException e) {		
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {			
			e.printStackTrace();
		}        
		return builder.compact();            
    } 
    
    public static String refreshJWT(UserDetails userDetails) {
    	
        
    	//The JWT signature algorithm which will be used to sign the token    	
              
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis+900000);
       
       
	
        
        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(apiKeySecretBytes);
       
        KeyFactory kf;
        PrivateKey privKey;
        JwtBuilder builder =null;
		try {
			kf = KeyFactory.getInstance("RSA");
			privKey = kf.generatePrivate(keySpec);
			builder = Jwts.builder()
					.setHeaderParam("alg", "RS256")
					.setHeaderParam("typ", "JWT")
					.setId("0B3EE6CBF68A2F7FCAAA472E11922015")
	                .setIssuedAt(now)
	                .setExpiration(exp)
	                .setSubject("DMSAPI")
	                .setAudience("DMSUI")
	                //.claim("pin", userDetails.getPassCode())
	                .claim("username", userDetails.getUserName())
	                .claim("usergroup", userDetails.getUserGroup())
	                .claim("loginid", userDetails.getLoginId())
	                .setIssuer("fd3b0648616674825338e8b1689e2407")
	                .signWith(signatureAlgorithm, privKey);
	        
		} catch (NoSuchAlgorithmException e) {		
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {			
			e.printStackTrace();
		}        
		return builder.compact();            
    } 
    
    public static UserDetails validateTokenAndGetJws(String token) throws DMSException{
    	System.out.println("Inside validate Token method"+token+"     ");
        try {
        	Claims claims = null;
            byte[] signingKey = SECRET_KEY.getBytes();
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(apiKeySecretBytes);
            UserDetails userdetails = new UserDetails();
            KeyFactory kf;
            PrivateKey privKey;
            JwtBuilder builder =null;
            
            
    		
    			kf = KeyFactory.getInstance("RSA");
    			privKey = kf.generatePrivate(keySpec);
            claims = Jwts.parser().setSigningKey(privKey).parseClaimsJws(token).getBody();
            System.out.println("username1"+ claims.get("username"));
            if (claims.getExpiration().getTime() <= System.currentTimeMillis()) {
            	throw new DMSException("Token expired. Please login again!!");
            }
            
            System.out.println("username2"+ claims.get("loginid"));
            if(claims.get("loginid") != null) {            	
            	userdetails.setLoginId(Long.parseLong(claims.get("loginid").toString()));
            }
           // userdetails.setLoginId(Long.parseLong(claims.get("loginid").toString()));
            System.out.println("username2"+ claims.get("username"));
            userdetails.setUserName((String) claims.get("username"));
            System.out.println("username3"+ claims.get("username"));
            userdetails.setUserGroup((String)claims.get("usergroup"));
            System.out.println("username4"+ claims.get("username"));
           // userdetails.setPassCode((String)claims.get("pin"));
            return userdetails;
            
        } catch (Exception ex) {
        	throw new DMSException("Authentication exception occured" + ex.getMessage());
        	
        }       
    }
    
   
}
