package com.ead.authuser.domain.services.implementations;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.ead.authuser.domain.exceptions.EntityNotFoundException;
import com.ead.authuser.domain.models.BaseEntity;
import com.ead.authuser.domain.services.CrudService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class DefaultCrudService implements CrudService {
	private final JpaRepositoryImplementation<BaseEntity, UUID> repository;

	@Override
	public void requireExistsById(UUID id) {
		if (!repository.exists(rootEntityById(id))) {
			throw new EntityNotFoundException();
		}
	}
	
	@Override
	public void deleteById(UUID id) {
		requireExistsById(id);
		repository.delete(rootEntityById(id));
	}
	
	private Specification<BaseEntity> rootEntityById(UUID id) {
		return (root, query, cb) -> cb.equal(root.get("id").as(UUID.class), id);
	}
}
