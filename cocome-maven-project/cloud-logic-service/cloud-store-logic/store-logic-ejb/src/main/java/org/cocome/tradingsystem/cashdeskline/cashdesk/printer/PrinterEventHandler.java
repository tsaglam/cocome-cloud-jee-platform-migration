/***************************************************************************
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
 ***************************************************************************/

package org.cocome.tradingsystem.cashdeskline.cashdesk.printer;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.cocome.tradingsystem.cashdeskline.events.CashAmountEnteredEvent;
import org.cocome.tradingsystem.cashdeskline.events.ChangeAmountCalculatedEvent;
import org.cocome.tradingsystem.cashdeskline.events.RunningTotalChangedEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleFinishedEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleStartedEvent;
import org.cocome.tradingsystem.cashdeskline.events.SaleSuccessEvent;
import org.cocome.tradingsystem.util.scope.CashDeskSessionScoped;

/**
 * Implements the cash desk event handler for the printer model. The event
 * handler is similar to a controller in that it converts incoming cash desk
 * messages to actions on the printer model. However, there is no view
 * associated with the controller.
 * 
 * @author Yannick Welsch
 * @author Lubomir Bulej
 * @author Tobias Pöppke
 * @author Robert Heinrich
 */

@CashDeskSessionScoped
class PrinterEventHandler implements IPrinterEventHandler, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2252238129675073065L;

	private static final Logger LOG =
			Logger.getLogger(PrinterEventHandler.class);

	private static final String DELIMITER = "----------------------------\n";

	//

	@Inject
	private IPrinterModel printer;

	//
	// Controller state
	//

	/**
	 * Enumerates the possible states of the printer controller.
	 */
	enum ControllerState {
		STOPPED,
		EXPECTING_ITEMS,
		EXPECTING_CASH_AMOUNT,
		EXPECTING_CHANGE_AMOUNT,
		SALE_COMPLETED,
	}

	private ControllerState controllerState;

	private double runningTotal;

	// Event handler methods
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes SaleStartedEvent event
			) {
		// Allow resetting of the sale
		if (this.controllerState == ControllerState.STOPPED 
				|| this.controllerState == ControllerState.EXPECTING_CASH_AMOUNT
				|| this.controllerState == ControllerState.EXPECTING_ITEMS) {
			this.controllerState = ControllerState.EXPECTING_ITEMS;
			this.startPrintout();

		} else {
			this.logUnexpectedState(event);
		}
	}

	private void startPrintout() {
		this.printer.tearOffPrintout();
		this.printer.printText(new Date().toString());
		this.printer.printText("\n");
		this.printer.printText(DELIMITER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes RunningTotalChangedEvent event) {
		final double runningTotal = event.getRunningTotal(); // NOCS
		LOG.debug("\trunningTotal: " + runningTotal);

		if (this.controllerState == ControllerState.EXPECTING_ITEMS) {
			this.runningTotal = runningTotal;
			this.printProductInfo(
					event.getProductName(), event.getProductPrice());

		} else {
			this.logUnexpectedState(event);
		}
	}

	/**
	 * 
	 * @param productName
	 * @param productPrice
	 */
	private void printProductInfo(
			final String productName, final double productPrice
			) {
		this.printer.printText(productName + ": " + productPrice + "\n");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes SaleFinishedEvent event) {
		if (this.controllerState == ControllerState.EXPECTING_ITEMS) {
			this.controllerState = ControllerState.EXPECTING_CASH_AMOUNT;
			this.printSaleTotal(this.runningTotal);

		} else {
			this.logUnexpectedState(event);
		}
	}

	private void printSaleTotal(final double total) {
		this.printer.printText(DELIMITER);
		this.printer.printText("Total: " + this.round(total) + "\n");
	}

	private double round(final double value) {
		return Math.rint(100 * value) / 100;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes CashAmountEnteredEvent event) {
		final double cashAmount = event.getCashAmount();
		LOG.debug("\tamount: " + cashAmount);

		if ((this.controllerState == ControllerState.EXPECTING_CASH_AMOUNT)) {
//				|| (this.controllerState == ControllerState.EXPECTING_CHANGE_AMOUNT)) {
			this.printCashAmount(cashAmount);
			this.controllerState = ControllerState.EXPECTING_CHANGE_AMOUNT;
		} else {
			this.logUnexpectedState(event);
		}
	}

	private void printCashAmount(final double cashAmount) {
		this.printer.printText("Cash received: " + cashAmount + "\n");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes ChangeAmountCalculatedEvent event) {
		final double changeAmount = event.getChangeAmount();
		LOG.debug("\tchangeAmount: " + changeAmount);

		if (this.controllerState == ControllerState.EXPECTING_CHANGE_AMOUNT) {
			this.printChangeAmount(changeAmount);
			this.controllerState = ControllerState.SALE_COMPLETED;

		} else {
			this.logUnexpectedState(event);
		}
	}

	private void printChangeAmount(final double changeAmount) {
		this.printer.printText("Change amount: " + changeAmount + "\n");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEvent(@Observes SaleSuccessEvent event) {
		this.finishPrintout();
		this.resetControllerState();
	}

	private void finishPrintout() {
		this.printer.printText(DELIMITER);
		this.printer.printText("Thank you for your purchase!\n");
	}

	//

	@PostConstruct
	private void resetControllerState() {
		this.controllerState = ControllerState.STOPPED;
		this.runningTotal = 0.0;
	}

	private void logUnexpectedState(Object event) {
		LOG.debug("\tevent " + event + " ignored - not expected in state " + this.controllerState);
	}

}
