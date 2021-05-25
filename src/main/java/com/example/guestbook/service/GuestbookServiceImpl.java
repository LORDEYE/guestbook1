package com.example.guestbook.service;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.dto.PageResultDTO;
import com.example.guestbook.entity.GuestBook;
import com.example.guestbook.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor //의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService{

        private final GuestbookRepository repository; //반드시 final로 선언

        @Override
        public Long register(GuestbookDTO dto) {
            log.info("DTO..................");
            log.info(dto);

            GuestBook entity = dtoToEntity(dto);
            log.info(entity);

            repository.save(entity);

            return entity.getGno();

        }

        public PageResultDTO<GuestbookDTO ,GuestBook> getList(PageRequestDTO requestDTO){
            Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

            Page<GuestBook> result = repository.findAll(pageable);
            Function<GuestBook,GuestbookDTO> fn = (entity->
                entityToDto(entity));

            return new PageResultDTO<>(result, fn);

        }


}
