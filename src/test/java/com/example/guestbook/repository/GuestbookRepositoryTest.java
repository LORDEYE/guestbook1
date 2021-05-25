package com.example.guestbook.repository;

import com.example.guestbook.entity.GuestBook;
import com.example.guestbook.entity.QGuestBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestbookRepositoryTest {
    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1,300).forEach(i ->{
            GuestBook guestBook= GuestBook.builder()
                    .title("title........"+i)
                    .content("content..........."+i)
                    .writer("user"+i)
                    .build();
            System.out.println(guestbookRepository.save(guestBook));
        });
    }

    @Test
    public void updateTest(){
        Optional<GuestBook> result = guestbookRepository.findById(300L);

        if(result.isPresent()){
            GuestBook guestBook = result.get();
            guestBook.chageTitle("Change Title........");
            guestBook.changeContent("Change Content");
            guestbookRepository.save(guestBook);
        }
    }
    
    //타이틀 1 조회
    @Test
    public void testQuery1(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestBook qGuestBook = QGuestBook.guestBook; //1

        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder(); //2

        BooleanExpression expression = qGuestBook.title.contains(keyword); //3

        builder.and(expression); //4

        Page<GuestBook> result = guestbookRepository.findAll(expression, pageable); //5

        result.stream().forEach(guestBook -> {

            System.out.println(guestBook);
        });

    }

    @Test
    public void testQuery2(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("gno").descending());
        QGuestBook qGuestBook = QGuestBook.guestBook;
        BooleanBuilder builder = new BooleanBuilder();
        String keyword = "1";
        BooleanExpression exTitle = qGuestBook.title.contains(keyword);
        BooleanExpression exContent = qGuestBook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent);
        builder.and(exAll);
        builder.and(qGuestBook.gno.gt(0L));

        Page<GuestBook> result = guestbookRepository.findAll(builder,pageable);

        result.forEach(guestBook -> {
            System.out.println(guestBook);
        });



    }
}