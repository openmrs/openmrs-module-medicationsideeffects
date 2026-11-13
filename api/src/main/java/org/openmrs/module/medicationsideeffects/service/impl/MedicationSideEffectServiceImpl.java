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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Drug;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.medicationsideeffects.dao.MedicationSideEffectDao;
import org.openmrs.module.medicationsideeffects.model.MedicationSideEffect;
import org.openmrs.module.medicationsideeffects.service.MedicationSideEffectService;

public class MedicationSideEffectServiceImpl extends BaseOpenmrsService implements MedicationSideEffectService {
	
	private MedicationSideEffectDao dao;
	
	public void setDao(MedicationSideEffectDao dao) {
		this.dao = dao;
	}
	
	@Override
	public MedicationSideEffect getByUuid(String uuid) {
		return dao.getByUuid(uuid);
	}
	
	@Override
	public List<MedicationSideEffect> getByDrug(Drug drug) {
		return dao.getByDrug(drug);
	}
	
	@Override
	public List<MedicationSideEffect> getAll(boolean includeVoided) {
		return dao.getAll(includeVoided);
	}
	
	@Override
	public MedicationSideEffect saveMedicationSideEffect(MedicationSideEffect sideEffect) {
		if (sideEffect.getSideEffectConcept() == null && StringUtils.isBlank(sideEffect.getSideEffectText())) {
			throw new APIException("Either a side effect concept or side effect text is required.");
		}
		return dao.saveOrUpdate(sideEffect);
	}
	
	@Override
	public MedicationSideEffect voidMedicationSideEffect(MedicationSideEffect sideEffect, String reason) {
		return dao.saveOrUpdate(sideEffect);
	}
}
