package com.bar.behdavarapplication.api;

import com.bar.behdavarbackend.business.api.UserBusiness;
import com.bar.behdavarbackend.dto.PersonDto;
import com.bar.behdavarbackend.dto.UserDto;
import com.bar.behdavarbackend.util.pagination.PagingRequest;
import com.bar.behdavarbackend.util.pagination.PagingResponse;
import com.bar.behdavarcommon.AuthorityConstant;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserRest {

    @Autowired
    UserBusiness userBusiness;

    @PreAuthorize("hasAuthority('" + AuthorityConstant.USER_VIEW + "')")
    @PostMapping("/find-by-id")
    public ResponseEntity<UserDto> findById(@RequestBody @NotNull Long id) {
        return new ResponseEntity<>(userBusiness.findById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('" + AuthorityConstant.USER_SAVE + "')")
    @PostMapping("/save")
    public ResponseEntity<Long> save(@Valid @RequestBody UserDto dto) {
        return new ResponseEntity<>(userBusiness.save(dto), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('" + AuthorityConstant.USER_UPDATE + "')")
    @PostMapping("/update")
    public ResponseEntity<Void> update(@RequestBody @Valid UserDto dto) {
        userBusiness.update(dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('" + AuthorityConstant.USER_DELETE + "')")
    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@RequestBody Long id) {
        userBusiness.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('" + AuthorityConstant.USER_SEARCH + "')")
    @PostMapping("/find-paging")
    public ResponseEntity<PagingResponse> findById(@RequestBody PagingRequest pageRequest) {
        return new ResponseEntity<>(userBusiness.findPaging(pageRequest), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('" + AuthorityConstant.USER_SEARCH + "')")
    @PostMapping("/find-suggestion")
    public ResponseEntity<List<UserDto>> findSuggestion(@RequestBody @NotBlank @Length(min = 3, max = 30) String suggest) {
        return new ResponseEntity(userBusiness.findSuggestion(suggest), HttpStatus.OK);
    }
}
