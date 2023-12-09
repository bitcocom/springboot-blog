package com.cos.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String content; // 섬머노트 라이브러리

    private int count;
    //Board에 User는 1건이기 때문에 바로가져오는 전략
    @ManyToOne(fetch = FetchType.EAGER) // Many=Board, One=User:한명의 회원이 여러 개의 게시글을 작성할 수 있다.
    @JoinColumn(name = "userId") // FK로 만들어진다.
    private User user;//DB는 오브젝트를 저장할 수 없다. FK
    // 연관관계 주인 = FK를 가진 오브젝트
    // 이 게시글의 답글정보(여러개)도 가져와야 한다.
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)// Reply에 있는 board와 연결(FK)
    //mappedBy란 연관관계의 주인이 아니다.(난 FK가 아니에요) DB에 컬럼을 만들지 마세요.
    @JsonIgnoreProperties({"board"})
    @OrderBy("id desc")
    private List<Reply> replys; // Reply에 FK가 있다.

    @CreationTimestamp
    private Timestamp createDate;
}
