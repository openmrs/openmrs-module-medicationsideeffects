/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.medicationsideeffects.service.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.Drug;
import org.openmrs.module.medicationsideeffects.dao.MedicationSideEffectDao;
import org.openmrs.module.medicationsideeffects.model.MedicationSideEffect;

public class MedicationSideEffectServiceImplTest {
	
	@Mock
	private MedicationSideEffectDao dao;
	
	private MedicationSideEffectServiceImpl service;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		service = new MedicationSideEffectServiceImpl();
		service.setDao(dao);
	}
	
	@Test
	public void getByUuid_shouldDelegateToDao() {
		MedicationSideEffect se = new MedicationSideEffect();
		when(dao.getByUuid("uuid-1")).thenReturn(se);
		
		assertSame(se, service.getByUuid("uuid-1"));
		verify(dao).getByUuid("uuid-1");
	}
	
	@Test
	public void getByDrug_shouldDelegateToDao() {
		Drug drug = new Drug();
		List<MedicationSideEffect> effects = Arrays.asList(new MedicationSideEffect());
		when(dao.getByDrug(drug)).thenReturn(effects);
		
		assertSame(effects, service.getByDrug(drug));
		verify(dao).getByDrug(drug);
	}
	
	@Test
	public void getAll_shouldDelegateToDao() {
		List<MedicationSideEffect> effects = Arrays.asList(new MedicationSideEffect());
		when(dao.getAll(true)).thenReturn(effects);
		
		assertSame(effects, service.getAll(true));
		verify(dao).getAll(true);
	}
	
	@Test
	public void save_shouldDelegateToDao() {
		MedicationSideEffect se = new MedicationSideEffect();
		when(dao.saveOrUpdate(se)).thenReturn(se);
		
		assertSame(se, service.save(se));
		verify(dao).saveOrUpdate(se);
	}
	
	@Test
	public void voidSideEffect_shouldReturnNullAndNotTouchDaoWhenGivenNull() {
		assertNull(service.voidSideEffect(null, "reason"));
		verify(dao, never()).saveOrUpdate(any(MedicationSideEffect.class));
	}
	
	@Test
	public void voidSideEffect_shouldNotReVoidAnAlreadyVoidedRecord() {
		MedicationSideEffect se = new MedicationSideEffect();
		se.setVoided(true);
		
		assertSame(se, service.voidSideEffect(se, "reason"));
		verify(dao, never()).saveOrUpdate(any(MedicationSideEffect.class));
	}
}
