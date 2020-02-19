# Cashfree Payout Integration Kit for Java

Below is an integration flow on how to use Cashfree's payouts.
Please go through the payout docs [here](https://dev.cashfree.com/payouts)
<br/>
This kit is linked to the standard transfer flow. Go [here](https://dev.cashfree.com/payouts/integrations/standard-transfer) to get a better understanding.
<br/>

## Functionalities

The following kit contains the following functionalities:
    <ol>
    <li> [getToken](https://dev.cashfree.com/api-reference/payouts-api#authorise): to get the auth token to be used in all          following calls.
    <li> [getBeneficiary](https://dev.cashfree.com/api-reference/payouts-api#get-beneficiary-details): to get beneficiary details/check if a beneficiary exists.
    <li> [createBeneficiaryEntity](https://dev.cashfree.com/api-reference/payouts-api#add-beneficiary): to create beneficiaries.
    <li> [requestTransfer](https://dev.cashfree.com/api-reference/payouts-api#standard-transfer): to create a payout transfer.
    <li> [getTransferStatus](https://dev.cashfree.com/api-reference/payouts-api#get-transfer-status): to get payout transfer status.
    </ol>

## Build Steps

follow the following build steps to compile the Integration kit:
  1. Download the code and cd into the directory containing the code.
  2. run the following command from your terminal to compile the code into an executable file:
      ```
      javac -cp .:json-simple-1.1.1.jar Main.java
      ```
      The code has a dependency on json simple for json parsing. Include this in your classpath while building(Hence -cp .:json-simple-1.1.1.jar 
      to include the jar in the classpath).
## Set Up

### Pre Requisites:
The following kit uses information stored in a config file. Before running the code for the first time open the config.json file
and add the relevant details:
  1. ClientId: This is a unique identifier that identifies the merchant. For more information please go [here](https://dev.cashfree.com/development/api/credentials).
  2. ClientSecret: Corresponding secret key for the given ClientId that helps Cashfree identify the merchant. For more information please go [here](https://dev.cashfree.com/development/api/credentials).
  3. Environment: Environment to be hit. The following values are accepted prod: for production, test: for the test environment.

### IP Whitelisting:

Your IP has to be whitelisted to hit Cashfree's server. For more information please go [here](https://dev.cashfree.com/development/api/ip-whitelisting).

### Beneficiary:
The following kit needs beneficiary details in order to check if the beneficiary exists and if it does not exist, 
create a beneficiary for the payout transfer. For more information on Beneficiaries please go [here](https://dev.cashfree.com/api-reference/payouts-api#beneficiary)

The kit reads beneficiary details from the config file. Under the beneDetails section. For a list of required fields go [here](https://dev.cashfree.com/api-reference/payouts-api#add-beneficiary).
Sample Fields to add a beneficiary using bankAccount and ifsc:
  1. beneId: uniqueId of the created beneficiary.
  2. name: beneficiary name.
  3. email: beneficiary email.
  4. phone: beneficiary phone.
  5. bankAccount: beneficiary's bank account.
  6. ifsc: corresponding ifsc.
  7. address1: beneficiary address.
  8. city: beneficiary city.
  9. state: beneficiary state.
  10. pincode: beneficiary pincode.
  
### Transfer Details:
To request a payout transfer certain information is needed. To get a better understanding of requesting a transfer go [here](https://dev.cashfree.com/api-reference/payouts-api#transfers).

Required Fields are:
  1. beneId: beneficiaryId to whom the transfer must be made to.
  2. amount: amount to be transferred.
  3. transferId: unique transfer id to identify the transfer.


## Usage

Once the config file is setup you can run the executable, to run the entire flow. Authorize, check and add beneficiary, 
request for a payout transfer and get the transfer status.

Run the following command in the terminal to run the executable:
```
      javac -cp .:json-simple-1.1.1.jar Main.java
```
You can change the necessary values in the config file as per your requirements and re-run the script whenever needed.

## Doubts

Reach out to techsupport@cashfree.com in case of doubts.
 


