/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.medicationsideeffects.web.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openmrs.Concept;
import org.openmrs.module.medicationsideeffects.model.MedicationSideEffect;
import org.openmrs.module.medicationsideeffects.model.SideEffectClassification;

/**
 * Unit tests for the pure (Context-free) logic of {@link MedicationSideEffectResource}.
 */
@RunWith(MockitoJUnitRunner.class)
public class MedicationSideEffectResourceTest {
	
	@Mock
	private Concept concept;
	
	private MedicationSideEffectResource resource;
	
	@Before
	public void setUp() {
		resource = new MedicationSideEffectResource();
	}
	
	@Test
	public void getDisplay_shouldPreferConceptDisplayString() {
		when(concept.getDisplayString()).thenReturn("Nausea");
		MedicationSideEffect sideEffect = new MedicationSideEffect();
		sideEffect.setSideEffectConcept(concept);
		sideEffect.setSideEffectText("free text that should be ignored");
		
		assertThat(resource.getDisplay(sideEffect), is("Nausea"));
	}
	
	@Test
	public void getDisplay_shouldFallBackToTextWhenNoConcept() {
		MedicationSideEffect sideEffect = new MedicationSideEffect();
		sideEffect.setSideEffectText("Mild headache");
		
		assertThat(resource.getDisplay(sideEffect), is("Mild headache"));
	}
	
	@Test
	public void getClassification_shouldReturnEnumName() {
		MedicationSideEffect sideEffect = new MedicationSideEffect();
		sideEffect.setClassification(SideEffectClassification.SERIOUS);
		
		assertThat(resource.getClassification(sideEffect), is("SERIOUS"));
	}
	
	@Test
	public void getClassification_shouldReturnNullWhenUnset() {
		assertThat(resource.getClassification(new MedicationSideEffect()), is(nullValue()));
	}
}
