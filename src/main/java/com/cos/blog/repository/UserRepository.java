package com.cos.blog.repository;

import com.cos.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
// DAO
// 자동으로 bean등록이 된다.
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
  // JPA Naming 쿼리
  // select * from user where useranem=1? and password=2?
  // User findByUsernameAndPassword(String username, String password);

  /*@Query(value = "select * from user where useranem=?1 and password=?2", nativeQuery = true)
  User login(String username, String password);*/
}
