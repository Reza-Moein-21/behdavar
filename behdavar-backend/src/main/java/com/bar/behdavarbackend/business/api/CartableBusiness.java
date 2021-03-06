package com.bar.behdavarbackend.business.api;

import com.bar.behdavarbackend.dto.AssignContractDto;
import com.bar.behdavarbackend.dto.CartableDto;
import com.bar.behdavarbackend.dto.UserInfoDto;
import com.bar.behdavarbackend.util.pagination.PagingRequest;
import com.bar.behdavarbackend.util.pagination.PagingResponse;

public interface CartableBusiness {
    CartableDto findById(Long id);

    Long save(CartableDto dto);

    void update(CartableDto dto);

    void delete(Long id);

    PagingResponse findPaging(PagingRequest pagingRequest);

    PagingResponse findPagingAll(PagingRequest pagingRequest);

    PagingResponse findPagingDocumentFlow(PagingRequest pagingRequest);

    void assignContract(AssignContractDto dto);

    UserInfoDto getUserInfo();
}
