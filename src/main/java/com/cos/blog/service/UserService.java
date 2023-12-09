package com.cos.blog.service;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// Service(서비스 레이어가 필요한 이유)
// 1. 트랜잭션 관리(송금 서비스 - update2번, 2개이상의 서비스, commit, rollback)
// 2. Layer
//스프링이 컴포넌트 스켄을 통해서 Bean에 등록을 해줌. IoC를 해준다.
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;
    @Transactional
    public void register(User user){
        String rawPassword=user.getPassword();
        String encPassword=encoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);
        userRepository.save(user); //commit. rollback
    }
    @Transactional(readOnly = true)
    public User search(String username){
       User user=userRepository.findByUsername(username).orElseGet(()->{
           return new User();
       });
       return user;
    }
    @Transactional
    public void update(User user) {
        // 수정시에는 영속성 컨텍스트 User오브젝트를 영속화시키고, 영속화된 User오브젝트를 수정
        User persistance=userRepository.findById(user.getId())
                .orElseThrow(()->{
                    return new IllegalArgumentException("회원 찾기 실패");
                }); //영속화 완료
        if(persistance.getOauth()==null || persistance.getOauth().equals("")) {
            String rawPassword = user.getPassword();
            String encPassword = encoder.encode(rawPassword);
            persistance.setPassword(encPassword);
            persistance.setEmail(user.getEmail());
        }
    }

    /*@Transactional(readOnly = true) // Select할때 트랜젝션 시작, 서비스 종료시에 트랜젝션 종료(정합성)
    public User login(User user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }*/
}
