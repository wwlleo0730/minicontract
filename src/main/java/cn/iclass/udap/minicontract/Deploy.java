package cn.iclass.udap.minicontract;

import cn.iclass.udap.minicontract.smartContract.MiniContract;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.geth.Geth;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;

public class Deploy {
    public static void main(String[] args) throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/aTFmVjYLRjSaUoS2xGeD"));
        Credentials credentials = Credentials.create("79972a6328ab7fd6bc90cae4b72fa41e9ad6ee376f39c0ebd5c4926157f1e97f");
        String contractAddress = MiniContract.deploy(web3j, credentials, BigInteger.valueOf(14000000000l), BigInteger.valueOf(7000000l)).send().getContractAddress();
        System.out.println(contractAddress);
    }
}
