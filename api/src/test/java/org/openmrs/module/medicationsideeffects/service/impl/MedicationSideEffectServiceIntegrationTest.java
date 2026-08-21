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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Drug;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.medicationsideeffects.model.MedicationSideEffect;
import org.openmrs.module.medicationsideeffects.model.SideEffectClassification;
import org.openmrs.module.medicationsideeffects.service.MedicationSideEffectService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class MedicationSideEffectServiceIntegrationTest extends BaseModuleContextSensitiveTest {
	
	private MedicationSideEffectService service;
	
	private Drug drug;
	
	@Before
	public void setUp() {
		service = Context.getService(MedicationSideEffectService.class);
		drug = Context.getConceptService().getAllDrugs(false).get(0);
	}
	
	private MedicationSideEffect newSideEffect() {
		MedicationSideEffect sideEffect = new MedicationSideEffect();
		sideEffect.setDrug(drug);
		sideEffect.setClassification(SideEffectClassification.SERIOUS);
		sideEffect.setSideEffectText("Signs of liver injury");
		sideEffect.setRecommendedAction("Stop and seek urgent care");
		return sideEffect;
	}
	
	@Test
	public void saveMedicationSideEffect_shouldPersistAndReadBackByDrugAndUuid() {
		MedicationSideEffect saved = service.saveMedicationSideEffect(newSideEffect());
		
		assertThat(saved.getId(), is(notNullValue()));
		assertThat(saved.getCreator(), is(notNullValue()));
		assertThat(saved.getDateCreated(), is(notNullValue()));
		
		Context.flushSession();
		Context.clearSession();
		
		MedicationSideEffect byUuid = service.getByUuid(saved.getUuid());
		assertThat(byUuid, is(notNullValue()));
		assertThat(byUuid.getSideEffectText(), is("Signs of liver injury"));
		assertThat(byUuid.getClassification(), is(SideEffectClassification.SERIOUS));
		
		List<MedicationSideEffect> byDrug = service.getByDrug(drug);
		assertThat(byDrug, contains(byUuid));
	}
	
	@Test
	public void saveMedicationSideEffect_shouldRejectRecordWithNeitherConceptNorText() {
		MedicationSideEffect invalid = newSideEffect();
		invalid.setSideEffectText(null);
		invalid.setSideEffectConcept(null);
		
		assertThrows(APIException.class, () -> service.saveMedicationSideEffect(invalid));
	}
	
	@Test
	public void voidMedicationSideEffect_shouldStampVoidMetadataAndExcludeFromGetByDrug() {
		MedicationSideEffect saved = service.saveMedicationSideEffect(newSideEffect());
		
		MedicationSideEffect voided = service.voidMedicationSideEffect(saved, "no longer relevant");
		
		assertThat(voided.getVoided(), is(true));
		assertThat(voided.getVoidReason(), is("no longer relevant"));
		assertThat(voided.getVoidedBy(), is(notNullValue()));
		assertThat(voided.getDateVoided(), is(notNullValue()));
		
		Context.flushSession();
		Context.clearSession();
		
		assertThat(service.getByDrug(drug), is(java.util.Collections.emptyList()));
		assertThat(service.getByUuid(voided.getUuid()), is(notNullValue()));
	}
}
