package com.cos.blog.repository;

import com.cos.blog.model.Board;
import com.cos.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
// DAO
// 자동으로 bean등록이 된다.
public interface BoardRepository extends JpaRepository<Board, Long> {

}
