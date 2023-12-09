package com.cos.blog.controller;

import com.cos.blog.auth.PrincipalDetail;
import com.cos.blog.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;
    //@AuthenticationPrincipal PrincipalDetail principal
    @GetMapping({"","/"})
    public String index(Model model, @PageableDefault(size=3, sort="id", direction= Sort.Direction.DESC) Pageable pageable){ // 컨트롤러에서 세션을 어떻게 찾는지?
        //System.out.println("로그인 사용자 아이디 : " + principal.getUsername());
        model.addAttribute("boards", boardService.getList(pageable));
        return "index";
    }

    //USER 권한이 필요함
    @GetMapping("/board/saveForm")
    public String saveForm(){
        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String findById(@PathVariable Long id, Model model){
        model.addAttribute("board", boardService.getByBoard(id));
        return "board/detail";
    }
    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable Long id, Model model){
        model.addAttribute("board",boardService.getByBoard(id));
        return "board/updateForm";
    }
}
