/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.medicationsideeffects;

/**
 * Shared constants for the Medication Side Effects module.
 */
public final class MedicationSideEffectsConstants {
	
	private MedicationSideEffectsConstants() {
	}
	
	public static final String MODULE_ID = "medicationsideeffects";
	
	// Privilege required to read side-effect records
	public static final String GET_SIDE_EFFECTS_PRIVILEGE = "Get Medication Side Effects";
	
	// Privilege required to create/update/void side-effect records.
	public static final String MANAGE_SIDE_EFFECTS_PRIVILEGE = "Manage Medication Side Effects";
}
