/*
 File: AppleDevicesRepository.java 
 Date 			    	Author 			Changes 
 May 10, 2020 	       NTT DATA 		Created
 */
package com.arits.docRepo.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class DMSSimulationProcRepository {
	/*
	 * @Autowired EntityManager entityManager;
	 */

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void loadData(String sql) {
		jdbcTemplate.execute(sql);
	}
	
	/*public long insertMessage(String message) {    
	    KeyHolder keyHolder = new GeneratedKeyHolder();

	    jdbcTemplate.update(connection -> {
	        PreparedStatement ps = connection
	          .prepareStatement(message);
	          
	          return ps;
	        }, keyHolder);

	        return (long) keyHolder.getKey();
	    }*/

	public int insertMessage(String message) {
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		 
		jdbcTemplate.update(
		  new PreparedStatementCreator() {
			  @Override
		    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		      return connection.prepareStatement(message, new String[] {"id"});
		    }

			
			
		  }, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	/*
	 * public List<R6AccountStatus> getAccountStatus(String billingAccountId) {
	 * 
	 * String plsql =
	 * "Select billing_account_id, status_type_key,id from INO_CRM.billing_account_status_tli "
	 * + "where billing_account_id='"+billingAccountId +
	 * "' and valid_to like '%01-JAN-00%' " + "order by billing_account_id asc" ;
	 * 
	 * return jdbcTemplate.query(plsql,(rs, rowNum) -> new R6AccountStatus(
	 * rs.getLong("billing_account_id"), rs.getString("status_type_key"),
	 * rs.getLong("id"))); }
	 */

}
