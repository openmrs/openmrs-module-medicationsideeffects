/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.medicationsideeffects.service;

import java.util.List;

import org.openmrs.Drug;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.medicationsideeffects.MedicationSideEffectsConstants;
import org.openmrs.module.medicationsideeffects.model.MedicationSideEffect;
import org.springframework.transaction.annotation.Transactional;

/**
 * MedicationSideEffectService for managing basic side effect actions.
 */
@Transactional
public interface MedicationSideEffectService extends OpenmrsService {
	
	@Transactional(readOnly = true)
	@Authorized(MedicationSideEffectsConstants.GET_SIDE_EFFECTS_PRIVILEGE)
	MedicationSideEffect getByUuid(String uuid);
	
	@Transactional(readOnly = true)
	@Authorized(MedicationSideEffectsConstants.GET_SIDE_EFFECTS_PRIVILEGE)
	List<MedicationSideEffect> getByDrug(Drug drug);
	
	@Transactional(readOnly = true)
	@Authorized(MedicationSideEffectsConstants.GET_SIDE_EFFECTS_PRIVILEGE)
	List<MedicationSideEffect> getAll(boolean includeVoided);
	
	@Transactional
	@Authorized(MedicationSideEffectsConstants.MANAGE_SIDE_EFFECTS_PRIVILEGE)
	MedicationSideEffect save(MedicationSideEffect sideEffect);
	
	@Transactional
	@Authorized(MedicationSideEffectsConstants.MANAGE_SIDE_EFFECTS_PRIVILEGE)
	MedicationSideEffect voidSideEffect(MedicationSideEffect sideEffect, String reason);
}
