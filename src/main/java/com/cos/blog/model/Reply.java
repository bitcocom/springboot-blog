package com.cos.blog.model;

import com.cos.blog.dto.ReplySaveRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reply { // 답변테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne
    @JoinColumn(name="boardId")
    private Board board; // 어느 게시글에 있는 답변인가

    @ManyToOne
    @JoinColumn(name="userId")
    private User user; // 누가 작성한 답변인가

    @CreationTimestamp
    private Timestamp createDate;

}
