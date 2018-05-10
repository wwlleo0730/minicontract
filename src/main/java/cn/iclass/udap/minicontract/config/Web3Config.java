package cn.iclass.udap.minicontract.config;

import cn.iclass.udap.minicontract.smartContract.MiniContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;

@Configuration
@Component
public class Web3Config {

    @Value("${miniContract.ethereumAddress}")
    public String contractAddress;

    @Value("${miniContract.ethereumPrivatekey}")
    public String serverPrivateKey;

    @Bean
    public Credentials getCredentials(){
        return Credentials.create(serverPrivateKey);
    }

    @Autowired
    public Web3j web3j;

    @Bean
    public MiniContract getMiniContract(){
        return MiniContract.load(contractAddress,web3j,getCredentials(), BigInteger.valueOf(14000000000l), BigInteger.valueOf(7000000l));
    }
}
