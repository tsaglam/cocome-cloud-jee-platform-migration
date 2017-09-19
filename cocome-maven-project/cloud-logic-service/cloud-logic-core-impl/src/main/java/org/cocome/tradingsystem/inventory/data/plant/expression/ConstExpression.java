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

package org.cocome.tradingsystem.inventory.data.plant.expression;

import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitOperation;

import javax.enterprise.context.Dependent;
import java.util.List;

/**
 * Plant-local expression that represents a constant list of operations
 *
 * @author Rudolf Bicozok
 */
@Dependent
public class ConstExpression extends Expression implements IConstExpression {
    private static final long serialVersionUID = 1L;

    private List<IProductionUnitOperation> operations;

    public List<IProductionUnitOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<IProductionUnitOperation> operations) {
        this.operations = operations;
    }
}
