/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.medicationsideeffects.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Concept;
import org.openmrs.Drug;

@Entity
@Table(name = "medication_side_effect")
public class MedicationSideEffect extends BaseOpenmrsData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "medication_side_effect_id")
	private Integer medicationSideEffectId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "drug_id", nullable = false)
	private Drug drug;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "classification", nullable = false)
	private SideEffectClassification classification;
	
	@ManyToOne
	@JoinColumn(name = "side_effect_concept_id")
	private Concept sideEffectConcept;
	
	@Column(name = "side_effect_text")
	private String sideEffectText;
	
	@Column(name = "recommended_action")
	private String recommendedAction;
	
	@Column(name = "notes")
	private String notes;
	
	public MedicationSideEffect() {
	}
	
	@Override
	public Integer getId() {
		return medicationSideEffectId;
	}
	
	@Override
	public void setId(Integer id) {
		this.medicationSideEffectId = id;
	}
	
	public Integer getMedicationSideEffectId() {
		return medicationSideEffectId;
	}
	
	public void setMedicationSideEffectId(Integer medicationSideEffectId) {
		this.medicationSideEffectId = medicationSideEffectId;
	}
	
	public Drug getDrug() {
		return drug;
	}
	
	public void setDrug(Drug drug) {
		this.drug = drug;
	}
	
	public SideEffectClassification getClassification() {
		return classification;
	}
	
	public void setClassification(SideEffectClassification classification) {
		this.classification = classification;
	}
	
	public Concept getSideEffectConcept() {
		return sideEffectConcept;
	}
	
	public void setSideEffectConcept(Concept sideEffectConcept) {
		this.sideEffectConcept = sideEffectConcept;
	}
	
	public String getSideEffectText() {
		return sideEffectText;
	}
	
	public void setSideEffectText(String sideEffectText) {
		this.sideEffectText = sideEffectText;
	}
	
	public String getRecommendedAction() {
		return recommendedAction;
	}
	
	public void setRecommendedAction(String recommendedAction) {
		this.recommendedAction = recommendedAction;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
