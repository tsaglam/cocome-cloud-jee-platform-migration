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

package org.cocome.tradingsystem.inventory.data.plant.parameter;

import org.cocome.tradingsystem.inventory.application.enterprise.parameter.IParameterTO;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseQuery;
import org.cocome.tradingsystem.inventory.data.plant.recipe.IPlantOperation;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Abstract class of {@link IParameterTO} for {@link IPlantOperation}
 *
 * @author Rudolf Biczok
 */
public abstract class PlantOperationParameter implements Serializable, IPlantOperationParameter {

    private static final long serialVersionUID = -2577328715744776645L;

    private long id;
    private String name;
    private String category;
    private IPlantOperation plantOperation;

    private long plantOperationId;

    @Inject
    private Instance<IEnterpriseQuery> enterpriseQueryInstance;

    private IEnterpriseQuery enterpriseQuery;

    @PostConstruct
    public void init() {
        enterpriseQuery = enterpriseQueryInstance.get();
        plantOperation = null;
    }

    /**
     * @return The id.
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * @param id Identifier value.
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The parameter name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name The parameter name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The the parameter category
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * @param category The parameter category
     */
    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the corresponding plant operation
     */
    public IPlantOperation getPlantOperation() throws NotInDatabaseException {
        if (plantOperation == null) {
            plantOperation = enterpriseQuery.queryPlantOperationByID(plantOperationId);
        }
        return plantOperation;
    }

    /**
     * @param plantOperation the corresponding plant operation
     */
    public void setPlantOperation(IPlantOperation plantOperation) {
        this.plantOperation = plantOperation;
    }

    /**
     * @return the id of the asspciated plant
     */
    @Override
    public long getPlantOperationId() {
        return plantOperationId;
    }

    /**
     * @param plantOperationId the corresponding plant operation id
     */
    @Override
    public void setPlantOperationId(long plantOperationId) {
        this.plantOperationId = plantOperationId;
    }
}
