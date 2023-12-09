package com.cos.blog.service;

import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// Service(서비스 레이어가 필요한 이유)
// 1. 트랜잭션 관리(송금 서비스 - update2번, 2개이상의 서비스, commit, rollback)
// 2. Layer
//스프링이 컴포넌트 스켄을 통해서 Bean에 등록을 해줌. IoC를 해준다.
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    /*@Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private BoardRepository boardRepository;*/
    @Transactional
    public void register(Board board, User user){
        board.setCount(0);
        board.setUser(user);
        boardRepository.save(board); //commit. rollback
    }
    @Transactional(readOnly = true)
    public Page<Board> getList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board getByBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 상세보기 실패:아이디를 찾을 수 없습니다.");
                });
    }
    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, Board requestBoard) {
        Board board=boardRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 찾기 실패:아이디를 찾을 수 없습니다.");
                }); //영속화 완료
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        // 해당 함수로 종료시(Service가 종료될 때)트랜잭션이 종료됩니다.
        // 이때 더티체킹 - 자동 업데이트가 됨. db flush
    }

    @Transactional
    public void replyRegister(ReplySaveRequestDto replySaveRequestDto) {
          replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
    }

    public void replyDelete(Long replyId) {
        replyRepository.deleteById(replyId);
    }
    /*@Transactional(readOnly = true) // Select할때 트랜젝션 시작, 서비스 종료시에 트랜젝션 종료(정합성)
    public User login(User user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }*/
}
