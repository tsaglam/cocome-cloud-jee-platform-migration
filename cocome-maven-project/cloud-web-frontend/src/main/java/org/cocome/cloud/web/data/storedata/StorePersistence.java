package org.cocome.cloud.web.data.storedata;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cocome.cloud.logic.stub.NotInDatabaseException_Exception;
import org.cocome.cloud.web.connector.enterpriseconnector.IEnterpriseQuery;
import org.cocome.cloud.web.connector.storeconnector.IStoreQuery;
import org.cocome.cloud.web.frontend.navigation.NavigationElements;
import org.cocome.cloud.web.frontend.util.Messages;

@Named
@ApplicationScoped
public class StorePersistence implements IStorePersistence {

	@Inject
	IEnterpriseQuery enterpriseQuery;

	@Inject
	IStoreQuery storeQuery;

	@Override
	public String updateStore(@NotNull StoreViewData store) throws NotInDatabaseException_Exception {
		store.updateStoreInformation();
		if (enterpriseQuery.updateStore(store)) {
			store.setEditingEnabled(false);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, Messages.get("store.update.success"), null));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages.get("store.update.failed"), null));

		return "error";
	}

	@Override
	public String createStore(long enterpriseID, @NotNull String name, @NotNull String location) throws NotInDatabaseException_Exception {
		if (enterpriseQuery.createStore(enterpriseID, name, location)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, Messages.get("store.create.success"), null));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, Messages.get("store.create.failed"), null));
		}

		return NavigationElements.SHOW_ENTERPRISES.getNavigationOutcome();
	}

	public boolean updateStockItem(@NotNull ProductWrapper stockItem) {
		return storeQuery.updateStockItem(stockItem.getOriginStore(), stockItem);
	}

	@Override
	public boolean orderProducts(StoreViewData store, Collection<OrderItem> items) {
		return storeQuery.orderProducts(store, items);
	}

	@Override
	public boolean rollInOrder(StoreViewData store, long orderID) {
		return storeQuery.rollInOrder(store, orderID);
	}

	@Override
	public boolean createStockItem(StoreViewData store, ProductWrapper product) {
		return storeQuery.createStockItem(store, product);
	}

	@Override
	public boolean updateStockItem(StoreViewData store, ProductWrapper stockItem) {
		return storeQuery.updateStockItem(store, stockItem);
	}
}
