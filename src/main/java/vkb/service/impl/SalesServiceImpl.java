package vkb.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;
import vkb.repository.SalesRepository;
import vkb.service.SalesService;


@Service
@Slf4j
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;
    private final ApiResponseUtil apiResponseUtil;


    public SalesServiceImpl(SalesRepository salesRepository, ApiResponseUtil apiResponseUtil) {
        this.salesRepository = salesRepository;
        this.apiResponseUtil = apiResponseUtil;


    }


    @Override
    @Transactional(readOnly = true)
    public AppApiResponse fetchAll(PageDto pageDto) {
        Pageable pageable = PageRequest.of(Integer.parseInt(pageDto.getPageNumber()), Integer.parseInt(pageDto.getPageSize()));
        String searchValue = pageDto.getSearchValue();
        if(searchValue==null || searchValue.trim().isEmpty())
            return apiResponseUtil.entityPagedList(salesRepository.findAll(pageable), pageable);
        else
            return apiResponseUtil.entityPagedList(salesRepository.findAllByBatchIdLike("%"+pageDto.getSearchValue()+"%", pageable), pageable);
    }




}
