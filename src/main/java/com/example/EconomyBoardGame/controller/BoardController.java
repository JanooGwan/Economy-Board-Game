package com.example.EconomyBoardGame.controller;

import com.example.EconomyBoardGame.entity.Board;
import com.example.EconomyBoardGame.entity.Member;
import com.example.EconomyBoardGame.entity.Post;
import com.example.EconomyBoardGame.service.BoardService;
import com.example.EconomyBoardGame.service.MemberService;
import com.example.EconomyBoardGame.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BoardService boardService;

    @GetMapping("/{boardId}")
    public String showBoard(@PathVariable Long boardId, Model model) {
        Board board = boardService.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Invalid board ID"));
        List<Post> posts = postService.findByBoard(board);
        model.addAttribute("board", board);
        model.addAttribute("posts", posts);
        return "board";
    }

    @GetMapping("/{boardId}/new")
    public String showNewPostForm(@PathVariable Long boardId, Model model) {
        Board board = boardService.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Invalid board ID"));
        model.addAttribute("post", new Post());
        model.addAttribute("boardId", boardId);
        return "writePost";
    }

    @PostMapping("/{boardId}")
    public String createPost(@PathVariable Long boardId, @Valid @ModelAttribute Post post, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder("제목 또는 내용이 다음과 같은 이유로 너무 깁니다:\n");
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessages.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n");
            });
            model.addAttribute("errorMessage", errorMessages.toString());
            return "writePost";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();
        Member member = memberService.findByNickname(nickname);
        Board board = boardService.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Invalid board ID"));

        post.setWriter(member);
        post.setBoard(board);
        post.setCreatedDate(LocalDateTime.now());
        post.setUpdatedDate(LocalDateTime.now());
        postService.save(post);
        return "redirect:/board/" + boardId;
    }


    @GetMapping("/{boardId}/{postId}")
    public String viewPost(@PathVariable Long boardId, @PathVariable Long postId, Model model) {
        Post post = postService.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        model.addAttribute("post", post);
        model.addAttribute("board", post.getBoard());
        return "viewPost";
    }

    @GetMapping("/{boardId}/{postId}/edit")
    public String showEditPostForm(@PathVariable Long boardId, @PathVariable Long postId, Model model) {
        Post post = postService.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        model.addAttribute("post", post);
        model.addAttribute("boardId", boardId);
        return "editPost";
    }

    @PostMapping("/{boardId}/{postId}/edit")
    public String editPost(@PathVariable Long boardId, @PathVariable Long postId, @ModelAttribute Post post, Model model) {
        Post existingPost = postService.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();

        System.out.println(existingPost.getWriter().getNickname());
        System.out.println(nickname);
        if (!existingPost.getWriter().getNickname().equals(nickname)) {
            model.addAttribute("errorMessage", "작성자 본인만 글을 수정할 수 있습니다.");
            model.addAttribute("post", existingPost);
            return viewPost(boardId, postId, model);
        }

        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setUpdatedDate(LocalDateTime.now());
        postService.save(existingPost);
        return "redirect:/board/" + boardId;
    }


    @PostMapping("/{boardId}/{postId}/delete")
    public String deletePost(@PathVariable Long boardId, @PathVariable Long postId, Model model) {
        Post post = postService.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nickname = auth.getName();

        if (!post.getWriter().getNickname().equals(nickname)) {
            model.addAttribute("errorMessage", "작성자 본인만 글을 삭제할 수 있습니다.");
            model.addAttribute("post", post);
            return viewPost(boardId, postId, model);
        }

        postService.deleteById(postId);
        return "redirect:/board/" + boardId;
    }

}