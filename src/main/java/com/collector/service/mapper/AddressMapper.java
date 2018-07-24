package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.AddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Address and its DTO AddressDTO.
 */
@Mapper(componentModel = "spring", uses = {StateMapper.class, CountryMapper.class})
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {

    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "country.id", target = "countryId")
    AddressDTO toDto(Address address);

    @Mapping(source = "stateId", target = "state")
    @Mapping(source = "countryId", target = "country")
    Address toEntity(AddressDTO addressDTO);

    default Address fromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }
}
