package com.bar.behdavarbackend.business.impl;

import com.bar.behdavarbackend.business.api.UserBusiness;
import com.bar.behdavarbackend.business.transformer.UserTransformer;
import com.bar.behdavarbackend.config.StartupPreparation;
import com.bar.behdavarbackend.dto.UserDto;
import com.bar.behdavarbackend.exception.BusinessException;
import com.bar.behdavarbackend.util.pagination.PagingExecutor;
import com.bar.behdavarbackend.util.pagination.PagingRequest;
import com.bar.behdavarbackend.util.pagination.PagingResponse;
import com.bar.behdavardatabase.entity.security.UserEntity;
import com.bar.behdavardatabase.repository.security.UserRepository;
import com.bar.behdavardatabase.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserBusinessImpl implements UserBusiness {

    private final UserRepository userRepository;

    @Autowired
    public UserBusinessImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto findByUserName(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new BusinessException("Username not found"));
        return UserTransformer.entityToDto(userEntity, new UserDto(), UserEntity.ROLES);
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userEntity -> UserTransformer.entityToDto(userEntity, new UserDto(), UserEntity.ROLE_DETAILS))
                .orElseThrow(() -> new BusinessException("error.User.not.found", id));
    }

    @Override
    public Long save(UserDto dto) {
        UserEntity userEntity = UserTransformer.dtoToEntity(dto, new UserEntity());
        return userRepository.save(userEntity).getId();
    }

    @Override
    public void update(UserDto dto) {
        if (dto.getUsername().equals(StartupPreparation.SUPERVISOR_USER)) {
            throw new BusinessException("error.invalid.operation");
        }
        UserEntity userEntity = UserTransformer.dtoToEntity(dto, userRepository
                .findById(dto.getId())
                .orElseThrow(() -> new BusinessException("error.User.not.found", dto.getId())));
        userRepository.save(userEntity);
    }

    @Override
    public void delete(Long id) {

        userRepository.findById(id).ifPresent(userEntity -> {
            if (!userEntity.getUsername().equals(StartupPreparation.SUPERVISOR_USER)) {
                userEntity.setDeleted(true);
                userRepository.save(userEntity);
            } else {
                throw new BusinessException("error.invalid.operation");
            }
        });
    }

    @Override
    public PagingResponse findPaging(PagingRequest pagingRequest) {
        PagingExecutor<UserEntity, Long> executor = new PagingExecutor<>(userRepository, pagingRequest);
        PagingResponse pagingResponse = executor.execute();
        if (pagingResponse.getData() != null) {
            List<UserDto> userDtos = new ArrayList<>();
            ((List<UserEntity>) pagingResponse.getData()).forEach(e ->
                    userDtos.add(UserTransformer.entityToDto(e, new UserDto(), UserEntity.ROLE_DETAILS))
            );
            pagingResponse.setData(this.filterSupervisorUser(userDtos));
        }
        return pagingResponse;
    }

    @Override
    public List<UserDto> findSuggestion(String suggest) {
        List<UserEntity> userEntities = userRepository.findSuggestion(suggest);
        List<UserDto> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userEntities)) {
            userEntities.forEach(e -> result.add(UserTransformer.entityToDto(e, new UserDto())));
        }
        return filterSupervisorUser(result);
    }

    // TODO must filter with query
    private List<UserDto> filterSupervisorUser(List<UserDto> userDtos) {
        if (!SecurityUtil.getCurrentUser().equals(StartupPreparation.SUPERVISOR_USER)) {
            return userDtos.stream().filter(user -> !user.getUsername().equals(StartupPreparation.SUPERVISOR_USER))
                    .collect(Collectors.toList());
        }
        return userDtos;
    }
}
