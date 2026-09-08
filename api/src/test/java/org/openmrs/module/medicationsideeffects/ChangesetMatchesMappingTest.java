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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ChangesetMatchesMappingTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Test
	public void changesetShouldDeclareEveryColumnTheEntityMaps() throws Exception {
		Set<String> mapped = new TreeSet<String>();
		sessionFactory.getCurrentSession().doWork(conn -> {
			try (Statement st = conn.createStatement();
			        ResultSet rs = st.executeQuery("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS "
			                + "WHERE UPPER(TABLE_NAME) = 'MEDICATION_SIDE_EFFECT'")) {
				while (rs.next()) {
					mapped.add(rs.getString(1).toLowerCase());
				}
			}
		});
		
		Set<String> declared = new TreeSet<String>();
		Element createTable = (Element) DocumentBuilderFactory.newInstance().newDocumentBuilder()
		        .parse(getClass().getResourceAsStream("/liquibase.xml")).getElementsByTagName("createTable").item(0);
		assertThat(createTable.getAttribute("tableName"), is("medication_side_effect"));
		collectColumnNames(createTable, declared);

		NodeList addColumns = createTable.getOwnerDocument().getElementsByTagName("addColumn");
		for (int i = 0; i < addColumns.getLength(); i++) {
			Element addColumn = (Element) addColumns.item(i);
			if ("medication_side_effect".equals(addColumn.getAttribute("tableName"))) {
				collectColumnNames(addColumn, declared);
			}
		}
		
		assertThat(declared, is(mapped));
	}
	
	private void collectColumnNames(Element change, Set<String> into) {
		NodeList columns = change.getElementsByTagName("column");
		for (int i = 0; i < columns.getLength(); i++) {
			into.add(((Element) columns.item(i)).getAttribute("name").toLowerCase());
		}
	}
}
