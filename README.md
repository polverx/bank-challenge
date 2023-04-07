## **THE EVERBANK (ONTOP BANK CHALLENGE)**

**Author:**  Oscar Eduardo Rivera Romero
****
### **Content**

- [Before starting](#before-starting)
- [Installing](#installing)
- [How to use](#how-to-use)
****
### **Before starting**

Make sure you have this installed. (Gradle is optional because it's included in the repo.)

| **Requirements:**     | **How to obtain**                                                           |
| --------------------- | --------------------------------------------------------------------------- |
| **Java**              | Obtain it here:  https://www.java.com/en/download/help/download_options.html|
| **Git**               | Guide: https://git-scm.com/book/en/v2/Getting-Started-Installing-Git        |
| **Gradle (optional)** | Guide here: https://gradle.org/install/                                     |

****
### **Installing**

#### **Cloning the repository**
First you need to clone the repository.
```
$ git clone https://github.com/polverx/bank-challenge.git
```

#### **Building the .jar**
Finally, execute this to build the .jar .
```
$ cd bank-challenge
$ ./gradlew build
```
****
### **How to use**

#### **Executing**
Once the jar has been created navigate to the directory where you built the JAR file:

```
$ cd bank-challenge/build/libs
```

Then run the JAR file using the following command:

```
$ java -jar bank-challenge-1.0.0.jar
```

This will start the application and make it available at http://localhost:8080.



#### **Local Endpoint**

Once the application is running, you can test it by sending a POST request to the following endpoint:

```
POST http://localhost:8080/api/v1/transactions
```

This endpoint accepts a JSON payload in the following format:

```
{
    "user_id": 1,
    "bank_account_id": "01",
    "amount": 100
}
```

This payload represents a transaction that transfers 100 units of currency from the bank account with ID "01" of the user with ID 1. 
Replace the values in this payload with your own data to test the application.


You can as well get all the transactions created by using the same endpoint, but as a GET

```
GET http://localhost:8080/api/v1/transactions
```
#### **MONGO ACCESS**
You can as well enter the Mongo DB and look for the documents created there.
You can find the User and TransactioHistory documents created.

On the Users document you can see the users ids and as well see what bank accounts they have.

You can use this mongo uri to access

```
mongodb+srv://admin:lco2buWW7GNG5Mqn@everbank.dvjjseh.mongodb.net/wallet
```



