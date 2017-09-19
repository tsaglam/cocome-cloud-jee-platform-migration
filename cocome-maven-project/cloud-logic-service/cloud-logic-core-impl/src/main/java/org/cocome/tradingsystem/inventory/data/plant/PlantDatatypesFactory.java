package org.cocome.tradingsystem.inventory.data.plant;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.inventory.application.enterprise.CustomProductTO;
import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.productionunit.ProductionUnitClassTO;
import org.cocome.tradingsystem.inventory.data.enterprise.ICustomProduct;
import org.cocome.tradingsystem.inventory.data.enterprise.IEnterpriseDataFactory;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.IProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.util.exception.NotInDatabaseException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Provider;

@Dependent
class PlantDatatypesFactory implements IPlantDataFactory {
    private static final Logger LOG = Logger.getLogger(PlantDatatypesFactory.class);

    @Inject
    private Provider<Plant> plantProvider;

    @Inject
    private Provider<ProductionUnitClass> productionUnitClassProvider;

    @Inject
    private IEnterpriseDataFactory enterpriseDatatypes;

    @Override
    public IPlant getNewPlant() {
        return plantProvider.get();
    }

    @Override
    public IProductionUnitClass getNewProductionUnitClass() {
        return productionUnitClassProvider.get();
    }

    @Override
    public ICustomProduct getNewCustomProduct() {
        //TODO
        return null;
    }

    @Override
    public IPlant convertToPlant(PlantTO plantTO) {
        IPlant plant = getNewPlant();
        plant.setName(plantTO.getName());
        plant.setLocation(plantTO.getLocation());
        plant.setId(plantTO.getId());
        plant.setEnterpriseId(plantTO.getEnterpriseTO().getId());
        return plant;
    }

    @Override
    public IProductionUnitClass convertToProductionUnitClass(ProductionUnitClassTO puc) {
        IProductionUnitClass pucTO = getNewProductionUnitClass();
        pucTO.setName(puc.getName());
        pucTO.setId(puc.getId());
        pucTO.setEnterpriseId(puc.getEnterprise().getId());
        return pucTO;
    }

    @Override
    public PlantTO fillPlantTO(IPlant plant)
            throws NotInDatabaseException {
        final PlantTO result = new PlantTO();
        result.setId(plant.getId());
        result.setName(plant.getName());
        result.setLocation(plant.getLocation());
        result.setEnterpriseTO(enterpriseDatatypes.fillEnterpriseTO(plant.getEnterprise()));

        LOG.debug(String.format("Got plant with id %d, name %s and enterprise %s.",
                result.getId(),
                result.getName(),
                result.getEnterpriseTO().getName()));

        return result;
    }

    @Override
    public ProductionUnitClassTO fillProductionUnitClassTO(IProductionUnitClass puc)
            throws NotInDatabaseException {
        final ProductionUnitClassTO result = new ProductionUnitClassTO();
        result.setId(puc.getId());
        result.setName(puc.getName());
        result.setEnterprise(enterpriseDatatypes.fillEnterpriseTO(puc.getEnterprise()));

        return result;
    }

    @Override
    public CustomProductTO fillCustomProductTO(ICustomProduct product) {
        //TODO
        return null;
    }

}
