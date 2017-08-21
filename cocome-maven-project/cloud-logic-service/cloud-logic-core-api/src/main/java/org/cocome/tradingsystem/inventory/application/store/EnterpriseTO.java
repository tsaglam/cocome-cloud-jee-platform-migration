/*
 *************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************************************************************
 */

package org.cocome.tradingsystem.inventory.application.store;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="EnterpriseTO", namespace="http://store.application.inventory.tradingsystem.cocome.org/")
@XmlRootElement(name="EnterpriseTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class EnterpriseTO implements Serializable {

	private static final long serialVersionUID = -7516714574375972227L;

	//

	@XmlElement(name="id", required=true)
	private long __id;

	@XmlElement(name="name", required=true)
	private String __name;

	//

	public long getId() {
		return __id;
	}

	public void setId(final long id) {
		__id = id;
	}

	public String getName() {
		return __name;
	}

	public void setName(final String name) {
		__name = name;
	}

}
