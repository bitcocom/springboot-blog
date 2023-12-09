package com.cos.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@DynamicInsert : insert시에 null인 필드를 제외시켜준다.
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 시퀀스, auto_increment

    @Column(nullable = false, length = 100, unique = true)
    private String username;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, length = 50)
    private String email;

    // admin, user, manager
    //@ColumnDefault("'user'")
    // DB에는 RoleType이라는 것이 없다.
    @Enumerated(EnumType.STRING)
    private RoleType role; // ADMIN, USER

    private String oauth;

    @CreationTimestamp // 시간이 자동으로 입력
    private Timestamp createDate;

}
