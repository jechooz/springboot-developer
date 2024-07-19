package me.shinsunyoung.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springbootdeveloper.dto.UpdateArticleRequest;
import me.shinsunyoung.springbootdeveloper.repository.BlogRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest //테스트용 애플리케이션 컨텍스트
@AutoConfigureMockMvc //MockMvc 생성 및 자동구성
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    //JSON데이터를 자바에서 사용하기 위해 자바객체로 변환하는 역직렬화할때 사용
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach //테스트 실행전 실행하는 메서드
    public void mockMvcSetUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }

    /**
     * 블로그 글 추가
     * given: 블로그글 추가에 필요한 요청객체를 만든다
     * when: 블로그글 추가 API에 요청을 보낸다. 이때 요청타입은 JSON이며,given절에서 미리 만들어둔 객체를 요청본문으로 함께보냄
     * then: 응답코드가 201 created인지 확인함. Blog를 전체조회해 크기가 1인지 확인하고 실제로 저장된 데이터와 요청값 비교
     * */
    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception{
        //given:
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        //객체 JSON으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        //when:
        //설정한 내용을 바탕으로 요청전송
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then:
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1); //크기가 1인지 검증
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    /**
     * 블로그 글목록 조회
     * given 블로그 글 저장
     * when 저장한 블로그글의 id값으로 api호출
     * then 응답코드가 200이고 반환받은 content와 title이 저장된 값과 같은지 확인
     * */
    @DisplayName("findArticle: 블로그  글조회에 성공한다.")
    @Test
    public void findArticles() throws Exception {
        //given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        //when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }

    /**
     * Given: 블로그 글을 저장합니다
     * When: 저장한 블로그 글의 id값으로 API를 호출합니다
     * Then:응답코드가 200 OK이고, 반환받은 content와 title이 저장된 값과 같은지 확인합니다
     * */
    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception{
        //given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        //when
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    /**
     * 글 하나 삭제
     * Given 블로그 글을 저장
     * When  저장한 블로그 글의 id값으로 삭제API를 호출
     * Then  응답코드가 200 OK이고, 블로그 글리스트를 전체조회해 조회한 배열크기가 0인지 확인합니다
     * */
    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception{
        //given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article saveArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        //when
        mockMvc.perform(delete(url, saveArticle.getId()))
                .andExpect(status().isOk());

        //then
        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();
    }

    /**
     * 글 수정
     * Given 블로그 글을 저장하고, 블로그 글 수정에 필요한 요청객체 생성
     * When UPDATE API로 수정요청 보냄. 요청타입은 JSON, given절에서 미리 만들어둔 객체를 요청본문으로 함께 보냄
     * Then 응답코드가 200 OK인지 확인함. 블로그 글id로 조회한 후에 값이 수정되었는지 확인
     * */

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다")
    @Test
    public void updateArticle() throws Exception{
        //given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final String newTitle = "new title";
        final String newContent = "new content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        //when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newTitle);
    }




}