package ispan.user.model;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserSecurityRepository extends JpaRepository<UserSecurityBean, String> {
	
	 	 
	 Optional<UserSecurityBean> findByUserResetPasswordTokenAndUserResetPasswordExpiresAfter(String token, Timestamp now);


	 @Transactional
	 @Modifying
	 @Query("UPDATE UserSecurityBean u SET u.userDeleted = :userDeleted, u.userDeletedTime = :userDeletedTime WHERE u.userID = :userID")
	 int updateUserDeleted(@Param("userDeleted") boolean userDeleted,@Param("userDeletedTime") Timestamp userDeletedTime, @Param("userID") String userID);
	 
	 @Transactional
	 @Modifying
	 @Query("UPDATE UserSecurityBean u SET u.userActive = :userActive WHERE u.userID = :userID")
	 int updateUserStatusByUserID(@Param("userActive") boolean userActive, @Param("userID") String userID);
	 
	 @Query("SELECT COUNT(u) FROM UserSecurityBean u WHERE u.userDeleted = false")
	 long countByExcludingUserDeleted();
	 
	 
}

