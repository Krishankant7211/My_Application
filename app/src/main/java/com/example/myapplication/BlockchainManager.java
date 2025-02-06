package com.example.myapplication;

import com.example.myapplication.contracts.DataRegistry;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import java.util.List;

public class BlockchainManager {
    private final Web3j web3j;
    private final DataRegistry dataRegistry;

    public BlockchainManager(String rpcUrl, String contractAddress) {
        // Initialize Web3j
        web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));

        // Load the smart contract
        TransactionManager transactionManager = new ClientTransactionManager(web3j, "0x3E3507BD15f19c40B1ACc67105B24B3cE388668f");
        dataRegistry = DataRegistry.load(contractAddress, web3j, transactionManager, new DefaultGasProvider());
    }

    // Method to submit data
    public void submitData(String documentName, String documentHash) throws Exception {
        dataRegistry.submitData(documentName, documentHash).send();
    }

    // Method to view all saved document data for the user
    public List<DataRegistry.Document> viewData() throws Exception {
        return dataRegistry.viewData().send();
    }
}

