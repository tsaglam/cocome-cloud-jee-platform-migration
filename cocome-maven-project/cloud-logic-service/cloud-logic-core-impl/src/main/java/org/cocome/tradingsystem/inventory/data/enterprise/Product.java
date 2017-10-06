/*
 ****************************************************************************
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
 **************************************************************************
 */

package org.cocome.tradingsystem.inventory.data.enterprise;

import org.cocome.tradingsystem.util.exception.NotInDatabaseException;
import org.cocome.tradingsystem.util.qualifier.EnterpriseRequired;
import org.cocome.tradingsystem.util.scope.IContextRegistry;
import org.cocome.tradingsystem.util.scope.RegistryKeys;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * This class represents a Product in the database
 *
 * @author Yannick Welsch
 * @author Tobias Pöppke
 */
@Dependent
public class Product extends AbstractProduct implements IProduct {
    private static final long serialVersionUID = -2577328715744776645L;

    private IProductSupplier supplier;

    // Inject Instance here because otherwise CDI complains about
    // missing the implementation bean when deploying
    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @Inject
    @EnterpriseRequired
    private Instance<IContextRegistry> registry;

    @PostConstruct
    private void initProduct() {
        enterpriseQuery = enterpriseQueryInstance.get();
    }

    @Override
    public IProductSupplier getSupplier() throws NotInDatabaseException {
        if (supplier == null) {
            long enterpriseID = registry.get().getLong(RegistryKeys.ENTERPRISE_ID);
            supplier = enterpriseQuery.querySupplierForProduct(enterpriseID, getBarcode());
        }
        return supplier;
    }

    @Override
    public void setSupplier(IProductSupplier supplier) {
        this.supplier = supplier;
    }
}
