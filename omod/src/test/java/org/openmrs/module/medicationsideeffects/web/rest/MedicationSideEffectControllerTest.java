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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.medicationsideeffects.model.MedicationSideEffect;
import org.openmrs.module.medicationsideeffects.model.SideEffectClassification;
import org.openmrs.module.medicationsideeffects.service.MedicationSideEffectService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.test.Util;
import org.openmrs.module.webservices.rest.web.v1_0.controller.RestControllerTestUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;

public class MedicationSideEffectControllerTest extends RestControllerTestUtils {
	
	private Drug drug;
	
	@Before
	public void setUpData() {
		drug = Context.getConceptService().getAllDrugs(false).get(0);
		MedicationSideEffect sideEffect = new MedicationSideEffect();
		sideEffect.setDrug(drug);
		sideEffect.setClassification(SideEffectClassification.COMMON);
		sideEffect.setSideEffectText("Nausea");
		sideEffect.setRecommendedAction("Take with food");
		Context.getService(MedicationSideEffectService.class).saveMedicationSideEffect(sideEffect);
		Context.flushSession();
	}
	
	@Test
	public void shouldSearchByDrugUuid() throws Exception {
		MockHttpServletRequest request = request(RequestMethod.GET, "medicationsideeffect");
		request.addParameter("drug", drug.getUuid());
		request.addParameter("v", "default");
		
		SimpleObject result = deserialize(handle(request));
		List<Object> hits = Util.getResultsList(result);
		
		assertEquals(1, hits.size());
		assertEquals("Nausea", Util.getByPath(hits.get(0), "display"));
		assertEquals("COMMON", Util.getByPath(hits.get(0), "classification"));
		assertEquals("Take with food", Util.getByPath(hits.get(0), "recommendedAction"));
	}
}
