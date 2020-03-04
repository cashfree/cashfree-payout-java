package com.cashfree.sdk.payout.workflow;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.concurrent.ThreadLocalRandom;

import com.cashfree.lib.constants.Constants.Environment;
import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.exceptions.ResourceDoesntExistException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.payout.clients.Payouts;
import com.cashfree.lib.payout.clients.Cashgram;
import com.cashfree.lib.payout.clients.Transfers;
import com.cashfree.lib.payout.clients.Validation;
import com.cashfree.lib.payout.clients.Beneficiary;

import com.cashfree.lib.payout.domains.CashgramDetails;
import com.cashfree.lib.payout.domains.BeneficiaryDetails;
import com.cashfree.lib.payout.domains.response.BatchTransferResponse;
import com.cashfree.lib.payout.domains.response.CfPayoutsResponse;
import com.cashfree.lib.payout.domains.response.GetBalanceResponse;
import com.cashfree.lib.payout.domains.request.BatchTransferRequest;
import com.cashfree.lib.payout.domains.request.SelfWithdrawalRequest;
import com.cashfree.lib.payout.domains.request.BulkValidationRequest;
import com.cashfree.lib.payout.domains.request.RequestTransferRequest;
import com.cashfree.lib.payout.domains.response.BulkValidationResponse;

import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.logger.VerySimpleFormatter;

public class cashfreePayoutJava {
  private static final Logger log = Logger.getLogger(cashfreePayoutJava.class.getName());

  static {
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(new VerySimpleFormatter());
    log.addHandler(consoleHandler);
  }

  public static void main(String[] args) {
    Payouts payouts =
        Payouts.getInstance(
            Environment.PRODUCTION,
            "CF1848EZPSGLHWP9IUE2Y",
            "b8df7784dd3f38911294d3597764dd43f3016a48");
    log.info("" + payouts.init());
    log.info("payouts initialized");
    boolean isTokenValid = payouts.verifyToken();
    if (!isTokenValid) return;
    log.info("Token is valid");
    Beneficiary beneficiary = new Beneficiary(payouts);
    log.info("Trying to fetch beneficiary based on beneId");
    try {
      log.info("" + beneficiary.getBeneficiaryDetails("JOHN18012"));
      log.info("" + beneficiary.getBeneficiaryId("00001111222233", "HDFC0000001"));
    } catch (ResourceDoesntExistException x) {
      log.warning(x.getMessage());
    }

    log.info("Beneficiary not found so Adding Beneficiary details");

    BeneficiaryDetails beneficiaryDetails =
        new BeneficiaryDetails()
            .setBeneId("JOHN18014")
            .setName("john doe")
            .setEmail("johndoe@cashfree.com")
            .setPhone("9876543210")
            .setBankAccount("00001111222233")
            .setIfsc("HDFC0000001")
            .setAddress1("ABC Street")
            .setCity("Bangalore")
            .setState("Karnataka")
            .setPincode("560001");

    try {
      log.info("" + beneficiary.addBeneficiary(beneficiaryDetails));
    } catch (ResourceAlreadyExistsException x) {
      log.warning(x.getMessage());
    }

    try {
      log.info("" + beneficiary.getBeneficiaryDetails("JOHN18012"));
      log.info("" + beneficiary.getBeneficiaryId("00001111222233", "HDFC0000001"));
    } catch (ResourceDoesntExistException x) {
      log.warning(x.getMessage());
    }

    log.info("After adding beneficiary calling request transfer");

    Transfers transfers = new Transfers(payouts);
    String transferId = "javasdktesttransferid" + ThreadLocalRandom.current().nextInt(0, 1000000);
    RequestTransferRequest request =
        new RequestTransferRequest()
            .setBeneId("VENKY_HDFC")
            .setAmount(new BigDecimal("1.00"))
            .setTransferId(transferId);
    try {
      log.info("" + transfers.requestTransfer(request));
    } catch (IllegalPayloadException x) {
      log.warning(x.getMessage());
    }

    log.info("Getting Transfer Status for the transaction");
    try {
      log.info("" + transfers.getTransferStatus(null, transferId));
    } catch (ResourceDoesntExistException x) {
      log.warning(x.getMessage());
    }

    log.info("Getting the Transfers");
    log.info("" + transfers.getTransfers(10, null, null));
  }
}