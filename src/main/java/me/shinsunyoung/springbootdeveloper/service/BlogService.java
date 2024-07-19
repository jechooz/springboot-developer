package me.shinsunyoung.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest;
import me.shinsunyoung.springbootdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor //final이 붙거나 @NotNull이 붙은 필드의 생성자추가
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    //블로그 글추가 메서드
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    //블로그 글목록 조회
    public List<Article> findAll(){
        return blogRepository.findAll();
    }

    //블로그 글하나 조회
    public Article findById(long id){
        return blogRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not found:"+ id));
    }

    //블로그 글하나 삭제
    public void delete(long id){
        blogRepository.deleteById(id);
    }

    //글 수정 메서드
    @Transactional
    public Article update(long id, UpdateArticleRequest request){

        //id로 기존글 찾아옴
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found:" + id));

        //요청내용으로 기존글 내용 변경
        article.update(request.getTitle(), request.getContent());

        return article;
    }

}
