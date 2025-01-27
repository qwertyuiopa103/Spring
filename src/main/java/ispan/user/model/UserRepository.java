package ispan.user.model;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<UserBean, String> {

	@Query(value = "SELECT NEXT VALUE FOR UserSeq", nativeQuery = true)
	Long getNextSequenceValue();

	boolean existsByUserEmail(String userEmail);

	boolean existsByUserPhone(String userPhone);

	@Query("SELECT u FROM UserBean u JOIN u.userSecurity s "
			+ "WHERE s.userResetPasswordToken = :token AND s.userResetPasswordExpires > :now")
	Optional<UserBean> findByResetPasswordTokenAndExpiry(@Param("token") String token, @Param("now") Timestamp now);

	Optional<UserBean> findByUserEmail(String userEmail);

	@Query("SELECT u FROM UserBean u WHERE u.userEmail = :acc OR u.userPhone = :acc")
	Optional<UserBean> findByPhoneOrEmail(@Param("acc") String acc);

//	 @Transactional
//	 @Modifying
//	 @Query("UPDATE UserBean u SET u.userDeleted = :userDeleted WHERE u.userID = :userID")
//	 int updateUserDeleted(@Param("userDeleted") boolean userDeleted, @Param("userID") String userID);
//	 
//	 @Transactional
//	 @Modifying
//	 @Query("UPDATE UserBean u SET u.userActive = :userActive WHERE u.userID = :userID")
//	 int updateUserStatusByUserID(@Param("userActive") boolean userActive, @Param("userID") String userID);
//	 
//	 @Query("SELECT COUNT(u) FROM UserBean u WHERE u.userDeleted = false")
//	 long countByExcludingUserDeleted();

	long countByUserRole(String userRole);

}
