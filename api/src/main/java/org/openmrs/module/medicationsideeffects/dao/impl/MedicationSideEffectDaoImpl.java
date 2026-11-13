/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.medicationsideeffects.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Drug;
import org.openmrs.module.medicationsideeffects.dao.MedicationSideEffectDao;
import org.openmrs.module.medicationsideeffects.model.MedicationSideEffect;

public class MedicationSideEffectDaoImpl implements MedicationSideEffectDao {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public MedicationSideEffect getByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MedicationSideEffect.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		return (MedicationSideEffect) criteria.uniqueResult();
	}
	
	@Override
	public MedicationSideEffect getById(Integer id) {
		return (MedicationSideEffect) sessionFactory.getCurrentSession().get(MedicationSideEffect.class, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MedicationSideEffect> getByDrug(Drug drug) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MedicationSideEffect.class);
		criteria.add(Restrictions.eq("drug", drug));
		criteria.add(Restrictions.eq("voided", false));
		return criteria.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MedicationSideEffect> getAll(boolean includeVoided) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MedicationSideEffect.class);
		if (!includeVoided) {
			criteria.add(Restrictions.eq("voided", false));
		}
		return criteria.list();
	}
	
	@Override
	public MedicationSideEffect saveOrUpdate(MedicationSideEffect sideEffect) {
		sessionFactory.getCurrentSession().saveOrUpdate(sideEffect);
		return sideEffect;
	}
}
