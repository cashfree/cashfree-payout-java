#  Cashfree Java Payout Integration 

Java bindings for interacting with the Cashfree API for AutoCollect. This is useful for merchants who are looking to automate their bank transfers programatically. 

# Using 

As you can see there are two files. "execute.java" is a guide to calling the API. <br />
NOTE : Ensure that "execute.java" and "cfAutoCollect.java" are in the same project.

# Setting Up

You will need to authenticate client by calling the client_auth function in your main as follows : 

```java
cashfreeUser newuser = new cashfreeUser();                                                                                                                                          
System.out.println(newuser.clientAuth("dummyClientId", "dummyClientSecret","TEST/PROD"));  
```



# Functionality

You can perform the following functions : 

**Add Beneficiary**
```
System.out.println(newuser.addBeneficiary("JOHN180120","john doe", "johndoe@cashfree.com", "9876543210","00091181202233","HDFC0000001","ABC Street","add 2","vpa","Bangalore", "Karnataka","560001" ));

```

**Request Transfer**
```
System.out.println(newuser.requestTransfer("JOHN18011","100","76723288672267867867","banktransfer","optional"));
```
**Get Transfer Status**

```
System.out.println(newuser.getTransferStatus("76723288672267867867"));
```
**Validate Bank Details**

```
System.out.println(newuser.bankDetailsValidation("Joh","9910115208", "00011020001772", "HDFC0000001"));
```

**Check Balance**

```
System.out.println(newuser.getBalance());
```

## Found a bug?

Report it at [https://github.com/cashfree/cashfree_payout_java/issues](https://github.com/cashfree/cashfree_payout_java/issues)

# Support

For further queries, reach us at techsupport@gocashfree.com .

********************************************************************************** 

