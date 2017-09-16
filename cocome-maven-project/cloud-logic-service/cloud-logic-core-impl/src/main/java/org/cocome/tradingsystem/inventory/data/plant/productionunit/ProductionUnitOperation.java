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

package org.cocome.tradingsystem.inventory.data.plant.productionunit;

import java.io.Serializable;

/**
 * Represents an atomic operation on a production unit
 *
 * @author Rudolf Biczok
 */
public class ProductionUnitOperation implements Serializable, IProductionUnitOperation {

    private static final long serialVersionUID = 1L;

    private long id;
    private String operationId;
    private IProductionUnitClass productionUnitClass;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getOperationId() {
        return operationId;
    }

    @Override
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    @Override
    public IProductionUnitClass getProductionUnitClass() {
        return productionUnitClass;
    }

    @Override
    public void setProductionUnitClass(IProductionUnitClass productionUnitClass) {
        this.productionUnitClass = productionUnitClass;
    }
}
