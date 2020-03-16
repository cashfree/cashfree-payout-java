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

  public cashfreePayoutJava() {
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(new VerySimpleFormatter());
    log.addHandler(consoleHandler);
  }

  public static void main(String[] args) {
    Payouts payouts =
        Payouts.getInstance(
            Environment.TEST,
                "",
                "");
    log.info("" + payouts.init());
    log.info("payouts initialized");
    boolean isTokenValid = payouts.verifyToken();
    if (!isTokenValid) return;

    log.info("Token is valid");


    Beneficiary beneficiary = new Beneficiary(payouts);
    BeneficiaryDetails beneficiaryDetails=new BeneficiaryDetails();
    boolean  flag=false;

    try {
      log.info("Trying to fetch beneficiary based on beneId");
      log.info("" + beneficiary.getBeneficiaryDetails("VENKY_HDFW"));
      beneficiaryDetails=beneficiary.getBeneficiaryDetails("VENKY_HDFW");
    } catch (ResourceDoesntExistException x) {

          log.warning(x.getMessage());
          log.info("Trying to fetch beneficiary based on account details");
          try{
            log.info("" + beneficiary.getBeneficiaryId("000100289877623", "SBIN0008752"));
            String beneId=beneficiary.getBeneficiaryId("000100289877623", "SBIN0008752");
            beneficiaryDetails=beneficiary.getBeneficiaryDetails(beneId);
            log.info(beneficiaryDetails.toString());
          } catch (ResourceDoesntExistException y) {
            log.warning(y.getMessage());
            flag=true;
          }
    }

    if (flag == true) {
      log.info("Beneficiary not found so Adding Beneficiary details");

       beneficiaryDetails =
          new BeneficiaryDetails()
              .setBeneId("bank_success")
              .setName("bsuccess")
              .setEmail("suneel@cashfree.com")
              .setPhone("7709736537")
              .setBankAccount("000100289877623")
              .setIfsc("SBIN0008752")
              .setAddress1("Bangalore")
              .setCity("Bangalore")
              .setState("Karnataka")
              .setPincode("560001");
    //  beneId=bank_success, name=bsuccess, email=suneel@cashfree.com, phone=7709736537, bankAccount=000100289877623, ifsc=SBIN0008752, address1=Bangalore, city=, state=, pincode=0)
      try {
        log.info("" + beneficiary.addBeneficiary(beneficiaryDetails));
        log.info("Beneficiary added");
      } catch (ResourceAlreadyExistsException x) {
        log.warning(x.getMessage());
      }
    }


    log.info("initiating Transfer Request");
    Transfers transfers = new Transfers(payouts);
    String transferId = "javasdktesdtransferid" + ThreadLocalRandom.current().nextInt(0, 1000000);

    RequestTransferRequest request =
        new RequestTransferRequest()
            .setBeneId(beneficiaryDetails.getBeneId())
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

    log.info("Getting the Transfer Details");
    log.info("" + transfers.getTransfers(10, null, null));
   // beneficiary.removeBeneficiary(beneficiaryDetails.getBeneId());
  }
}