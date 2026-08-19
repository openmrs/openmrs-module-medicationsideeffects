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

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.medicationsideeffects.model.MedicationSideEffect;
import org.openmrs.module.medicationsideeffects.model.SideEffectClassification;
import org.openmrs.module.medicationsideeffects.service.MedicationSideEffectService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.EmptySearchResult;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;

/**
 * Read-only REST resource exposing medication side-effect records.
 * <p>
 * The only supported access pattern is a search by drug: {@code GET
 * /ws/rest/v1/medicationsideeffect?drug={drugUuid}}, which returns every non-voided side effect
 * linked to the given drug. Individual records are also retrievable by uuid.
 */
@Resource(name = RestConstants.VERSION_1
        + "/medicationsideeffect", supportedClass = MedicationSideEffect.class, supportedOpenmrsVersions = { "2.4.* - 9.*" })
public class MedicationSideEffectResource extends DelegatingCrudResource<MedicationSideEffect> {
	
	private MedicationSideEffectService getService() {
		return Context.getService(MedicationSideEffectService.class);
	}
	
	@Override
	public MedicationSideEffect getByUniqueId(String uniqueId) {
		return getService().getByUuid(uniqueId);
	}
	
	@Override
	public MedicationSideEffect newDelegate() {
		return new MedicationSideEffect();
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		if (rep instanceof DefaultRepresentation || rep instanceof RefRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("display");
			description.addProperty("classification");
			description.addProperty("sideEffectText");
			description.addProperty("recommendedAction");
			description.addProperty("notes");
			description.addProperty("drug", Representation.REF);
			description.addProperty("sideEffectConcept", Representation.REF);
			description.addProperty("voided");
			description.addSelfLink();
			return description;
		} else if (rep instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("display");
			description.addProperty("classification");
			description.addProperty("sideEffectText");
			description.addProperty("recommendedAction");
			description.addProperty("notes");
			description.addProperty("drug", Representation.DEFAULT);
			description.addProperty("sideEffectConcept", Representation.DEFAULT);
			description.addProperty("voided");
			description.addProperty("auditInfo");
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	@PropertyGetter("display")
	public String getDisplay(MedicationSideEffect sideEffect) {
		if (sideEffect.getSideEffectConcept() != null) {
			return sideEffect.getSideEffectConcept().getDisplayString();
		}
		return sideEffect.getSideEffectText();
	}
	
	@PropertyGetter("classification")
	public String getClassification(MedicationSideEffect sideEffect) {
		SideEffectClassification classification = sideEffect.getClassification();
		return classification == null ? null : classification.name();
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		String drugUuid = context.getParameter("drug");
		if (StringUtils.isBlank(drugUuid)) {
			throw new ResourceDoesNotSupportOperationException();
		}
		Drug drug = Context.getConceptService().getDrugByUuid(drugUuid);
		if (drug == null) {
			return new EmptySearchResult();
		}
		return new NeedsPaging<MedicationSideEffect>(getService().getByDrug(drug), context);
	}
	
	@Override
	public MedicationSideEffect save(MedicationSideEffect delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	protected void delete(MedicationSideEffect delegate, String reason, RequestContext context) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(MedicationSideEffect delegate, RequestContext context) {
		throw new ResourceDoesNotSupportOperationException();
	}
}
