package com.collector.service.mapper;

import com.collector.domain.*;
import com.collector.service.dto.PublisherDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Publisher and its DTO PublisherDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PublisherMapper extends EntityMapper<PublisherDTO, Publisher> {



    default Publisher fromId(Long id) {
        if (id == null) {
            return null;
        }
        Publisher publisher = new Publisher();
        publisher.setId(id);
        return publisher;
    }
}
