package com.bar.behdavarbackend.business.api;

import com.bar.behdavarbackend.dto.BankDto;
import com.bar.behdavarbackend.util.pagination.PagingRequest;
import com.bar.behdavarbackend.util.pagination.PagingResponse;
import com.bar.behdavardatabase.entity.BankBranchEntity;

public interface BankBusiness {
    BankBranchEntity findById(Long id);

    Long save(BankDto dto);

    void update(BankDto dto);

    void delete(Long id);

    PagingResponse findPaging(PagingRequest pagingRequest);
}
