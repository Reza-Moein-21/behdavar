package com.bar.behdavarbackend.business.impl;

import com.bar.behdavarbackend.business.api.ExcelReaderBusiness;
import com.bar.behdavarbackend.business.transformer.InputExcelLendingTransformer;
import com.bar.behdavarbackend.business.transformer.InputExcelPersonTransformer;
import com.bar.behdavarbackend.business.transformer.UserTransformer;
import com.bar.behdavarbackend.dto.InputExcelDebtorDto;
import com.bar.behdavarbackend.dto.InputExcelDto;
import com.bar.behdavarbackend.dto.InputExcelGuarantorDto;
import com.bar.behdavarbackend.dto.InputExcelLendingDto;
import com.bar.behdavarbackend.exception.BusinessException;
import com.bar.behdavarcommon.enumeration.ContractStatus;
import com.bar.behdavarcommon.enumeration.ContractType;
import com.bar.behdavarcommon.enumeration.ContractWeight;
import com.bar.behdavarcommon.enumeration.PhoneType;
import com.bar.behdavardatabase.entity.*;
import com.bar.behdavardatabase.entity.security.UserEntity;
import com.bar.behdavardatabase.repository.*;
import com.bar.behdavardatabase.repository.security.UserRepository;
import com.bar.behdavardatabase.util.SecurityUtil;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service(ExcelReaderBusinessImpl.BEAN_NAME)
public class ExcelReaderBusinessImpl implements ExcelReaderBusiness {
    public static final String BEAN_NAME = "ExcelReaderBusinessImpl";

    @Autowired
    InputExcelLendingRepository inputExcelLendingRepository;

    @Autowired
    InputExcelGuarantorRepository inputExcelGuarantorRepository;

    @Autowired
    InputExcelDebtorRepository inputExcelDebtorRepository;

    @Autowired
    InputExcelRepository inputExcelRepository;

    @Autowired
    PersonBusinessImpl personBusiness;


    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    LendingRepository lendingRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    GuarantorRepository guarantorRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CartableRepository cartableRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public void readAndSave(InputExcelDto dto) {
        byte[] bytes = Base64Utils.decodeFromString(dto.getContent());
        InputExcelEntity inputExcelEntity = new InputExcelEntity();
        inputExcelEntity.setContent(bytes);
        inputExcelEntity.setFileName(dto.getFileName());
        inputExcelEntity = inputExcelRepository.save(inputExcelEntity);
        InputExcelEntity finalInputExcelEntity = inputExcelEntity;


        List<InputExcelLendingDto> inputExcelLendingDtos = Poiji.fromExcel(new ByteArrayInputStream(bytes), PoijiExcelType.XLSX, InputExcelLendingDto.class);
        if (!CollectionUtils.isEmpty(inputExcelLendingDtos)) {
            List<InputExcelLendingEntity> inputExcelLendingEntities = new ArrayList<>();
            inputExcelLendingDtos.forEach(inputExcelLendingDto -> {
                InputExcelLendingEntity entity = InputExcelLendingTransformer.dtoToEntity(inputExcelLendingDto, new InputExcelLendingEntity());
                entity.setInputExcel(finalInputExcelEntity);
                inputExcelLendingEntities.add(entity);
            });
            inputExcelLendingRepository.saveAll(inputExcelLendingEntities);
        } else {
            throw new BusinessException("invalid.input.excel.file");
        }

        List<InputExcelGuarantorDto> inputExcelGuarantorDtos = Poiji.fromExcel(new ByteArrayInputStream(bytes), PoijiExcelType.XLSX, InputExcelGuarantorDto.class);
        if (!CollectionUtils.isEmpty(inputExcelGuarantorDtos)) {
            List<InputExcelGuarantorEntity> inputExcelGuarantorEntities = new ArrayList<>();
            inputExcelGuarantorDtos.forEach(excelGuarantorDto -> {
                InputExcelGuarantorEntity entity = (InputExcelGuarantorEntity) InputExcelPersonTransformer.dtoToEntity(excelGuarantorDto, new InputExcelGuarantorEntity());
                entity.setInputExcel(finalInputExcelEntity);
                inputExcelGuarantorEntities.add(entity);
            });
            inputExcelGuarantorRepository.saveAll(inputExcelGuarantorEntities);
        } else {
            throw new BusinessException("invalid.input.excel.file");
        }

        List<InputExcelDebtorDto> inputExcelDebtorDtos = Poiji.fromExcel(new ByteArrayInputStream(bytes), PoijiExcelType.XLSX, InputExcelDebtorDto.class);
        if (!CollectionUtils.isEmpty(inputExcelDebtorDtos)) {
            List<InputExcelDebtorEntity> inputExcelDebtorEntities = new ArrayList<>();
            inputExcelDebtorDtos.forEach(inputExcelDebtorDto -> {
                InputExcelDebtorEntity entity = (InputExcelDebtorEntity) InputExcelPersonTransformer.dtoToEntity(inputExcelDebtorDto, new InputExcelDebtorEntity());
                entity.setInputExcel(finalInputExcelEntity);
                inputExcelDebtorEntities.add(entity);
            });
            inputExcelDebtorRepository.saveAll(inputExcelDebtorEntities);
        } else {
            throw new BusinessException("invalid.input.excel.file");
        }


    }

    @Transactional
    public void convert(Long inputExcelId) {

        List<InputExcelLendingEntity> inputExcelLendingEntities = inputExcelLendingRepository.findByInputExcelId(inputExcelId);
        if (!inputExcelLendingEntities.isEmpty()) {
            inputExcelLendingEntities.forEach(excelLendingEntity -> {
                ContractEntity contractEntity = contractRepository.findByContractNumber(excelLendingEntity.getContractNumber());

                if (contractEntity != null) {
                    if (contractEntity.getLending() != null) {
                        LendingEntity lendingEntity = lendingRepository.findById(contractEntity.getLending().getId()).get();
                        lendingEntity.setMasterAmount(excelLendingEntity.getDebtAmount());
                        lendingEntity.setDefferedAmount(excelLendingEntity.getInstallmentAmount());
                        lendingEntity.setDefferedCount(excelLendingEntity.getInstallmentCount());
                        lendingEntity.setDifferedInstallmentCount(excelLendingEntity.getDifferedInstallmentCount());
                        lendingEntity.setDifferedInstallmentCount(excelLendingEntity.getDifferedInstallmentCount());
                        lendingRepository.save(lendingEntity);
                        return;
                    }

                }

                // grantors
                contractEntity = new ContractEntity();
                contractEntity.setContractNumber(excelLendingEntity.getContractNumber());
                contractEntity.setContractStatus(ContractStatus.AVAILABLE);
                if (excelLendingEntity.getMachine() != null){
                    contractEntity.setContractType(ContractType.CARS);
                    ProductEntity productEntity = new ProductEntity();
                    productEntity.setProductName(excelLendingEntity.getMachine());
                    productEntity.setProductPlate(excelLendingEntity.getPlaqueNumber());
                    productEntity.setProductShasiNumber(excelLendingEntity.getShasiNumber());
                    productRepository.save(productEntity);
                    contractEntity.setProduct(productEntity);
                } else {
                    contractEntity.setContractType(ContractType.BANKS);
                }

                LendingEntity lendingEntity = new LendingEntity();
                lendingEntity.setMasterAmount(excelLendingEntity.getDebtAmount());
                lendingEntity.setDefferedAmount(excelLendingEntity.getInstallmentAmount());
                lendingEntity.setDefferedCount(excelLendingEntity.getInstallmentCount());
                lendingEntity.setDifferedInstallmentCount(excelLendingEntity.getDifferedInstallmentCount());
                lendingEntity.setDifferedInstallmentCount(excelLendingEntity.getDifferedInstallmentCount());
                lendingRepository.save(lendingEntity);
                contractEntity.setLending(lendingEntity);
                contractEntity.setContractWeight(ContractWeight.LEVEL1);
                //condition for contractWeight
            /*    if (excelLendingEntity.getAmount() != null) {

                }*/
                InputExcelGuarantorEntity byInputExcelIdAndContractNumber = inputExcelGuarantorRepository.findByInputExcelIdAndContractNumber(inputExcelId, excelLendingEntity.getContractNumber());
                if (byInputExcelIdAndContractNumber != null) {
                    PersonEntity personEntity = new PersonEntity();
                    personEntity.setFullName(byInputExcelIdAndContractNumber.getLastName());
                    personEntity.setFatherName(byInputExcelIdAndContractNumber.getFatherName());
                    personEntity.setNationalCode(byInputExcelIdAndContractNumber.getNationalCode());
                    Long personId = personBusiness.save(personEntity);
                    personEntity.setId(personId);

                    if (byInputExcelIdAndContractNumber.getMobile1() != null) {
                        ContactEntity contactEntity = new ContactEntity();
                        contactEntity.setNumber(byInputExcelIdAndContractNumber.getMobile1());
                        contactEntity.setPhoneType(PhoneType.MOBILE);
                        contactEntity.setPerson(personEntity);
                        contactRepository.save(contactEntity);
                    }

                    if (byInputExcelIdAndContractNumber.getMobile2() != null) {
                        ContactEntity contactEntity = new ContactEntity();
                        contactEntity.setNumber(byInputExcelIdAndContractNumber.getMobile2());
                        contactEntity.setPhoneType(PhoneType.MOBILE);
                        contactEntity.setPerson(personEntity);
                        contactRepository.save(contactEntity);
                    }

                    if (byInputExcelIdAndContractNumber.getTel1() != null) {
                        ContactEntity contactEntity = new ContactEntity();
                        contactEntity.setNumber(byInputExcelIdAndContractNumber.getTel1());
                        contactEntity.setPhoneType(PhoneType.PHONE);
                        contactEntity.setPerson(personEntity);
                        contactRepository.save(contactEntity);
                    }

                    if (byInputExcelIdAndContractNumber.getTel2() != null) {
                        ContactEntity contactEntity = new ContactEntity();
                        contactEntity.setNumber(byInputExcelIdAndContractNumber.getTel2());
                        contactEntity.setPhoneType(PhoneType.PHONE);
                        contactEntity.setPerson(personEntity);
                        contactRepository.save(contactEntity);
                    }

                    if (byInputExcelIdAndContractNumber.getAddress() != null) {
                        AddressEntity addressEntity = new AddressEntity();
                        addressEntity.setPerson(personEntity);
                        addressEntity.setDescription(byInputExcelIdAndContractNumber.getAddress());
                        addressRepository.save(addressEntity);
                    }
                    GuarantorEntity guarantorEntity = new GuarantorEntity();
                    guarantorEntity.setContract(contractEntity);
                    guarantorEntity.setPerson(personEntity);
                    contractRepository.save(contractEntity);
                    guarantorRepository.save(guarantorEntity);

                    if (excelLendingEntity.getExpertCode() != null) {
                        UserEntity userExpertEntity = userRepository.findByCode(excelLendingEntity.getExpertCode());
                        if (userExpertEntity != null) {
                            CartableEntity cartableEntity = new CartableEntity();
                            cartableEntity.setActive(true);
                            cartableEntity.setContract(contractEntity);
                            cartableEntity.setReceiver(userExpertEntity);
                            UserEntity sender = UserTransformer.createEntityForRelation(SecurityUtil.getCurrentUserId());
                            cartableEntity.setSender(sender);
                            cartableRepository.save(cartableEntity);
                        } else {
                            throw new BusinessException("user.with.code"+excelLendingEntity.getExpertCode()+"not.found");
                        }
                    } else {
                        throw new BusinessException("contract.with.contractNumber"+excelLendingEntity.getContractNumber()+"not.have.expertCode");
                    }


                }
                // end of grantor
                //debtors
                InputExcelDebtorEntity debtorEntities = inputExcelDebtorRepository.findByInputExcelIdAndContractNumber(inputExcelId, excelLendingEntity.getContractNumber());
                if (debtorEntities != null) {
                    PersonEntity personEntity = new PersonEntity();
                    personEntity.setFullName(debtorEntities.getLastName());
                    personEntity.setFatherName(debtorEntities.getFatherName());
                    personEntity.setNationalCode(debtorEntities.getNationalCode());
                    personEntity.setId(personBusiness.save(personEntity));
                    personBusiness.save(personEntity);

                    if (debtorEntities.getMobile1() != null) {
                        ContactEntity contactEntity = new ContactEntity();
                        contactEntity.setNumber(debtorEntities.getMobile1());
                        contactEntity.setPhoneType(PhoneType.MOBILE);
                        contactEntity.setPerson(personEntity);
                        contactRepository.save(contactEntity);
                    }

                    if (debtorEntities.getMobile2() != null) {
                        ContactEntity contactEntity = new ContactEntity();
                        contactEntity.setNumber(debtorEntities.getMobile2());
                        contactEntity.setPhoneType(PhoneType.MOBILE);
                        contactEntity.setPerson(personEntity);
                        contactRepository.save(contactEntity);
                    }

                    if (debtorEntities.getTel1() != null) {
                        ContactEntity contactEntity = new ContactEntity();
                        contactEntity.setNumber(debtorEntities.getTel1());
                        contactEntity.setPhoneType(PhoneType.PHONE);
                        contactEntity.setPerson(personEntity);
                        contactRepository.save(contactEntity);
                    }

                    if (debtorEntities.getTel2() != null) {
                        ContactEntity contactEntity = new ContactEntity();
                        contactEntity.setNumber(debtorEntities.getTel2());
                        contactEntity.setPhoneType(PhoneType.PHONE);
                        contactEntity.setPerson(personEntity);
                        contactRepository.save(contactEntity);
                    }

                    if (debtorEntities.getAddress() != null) {
                        AddressEntity addressEntity = new AddressEntity();
                        addressEntity.setPerson(personEntity);
                        addressEntity.setDescription(debtorEntities.getAddress());
                        addressRepository.save(addressEntity);
                    }
                    CustomerEntity customerEntity = new CustomerEntity();
                    customerEntity.setContract(contractEntity);
                    customerEntity.setPerson(personEntity);
                    customerRepository.save(customerEntity);
                    //end of debtor
                }
            });
        }
    }
}