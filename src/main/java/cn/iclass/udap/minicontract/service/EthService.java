package cn.iclass.udap.minicontract.service;

import cn.iclass.udap.minicontract.smartContract.MiniContract;
import cn.iclass.udap.minicontract.util.IClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class EthService {

    @Autowired
    public MiniContract miniContract;

    @Autowired
    public IClassUtil iClassUtil;

    @Autowired
    public Web3j web3j;

    public String mint(String A, String B, String Aphone, String Bphone, String tilte, String content, String sound, String picture) throws ExecutionException, InterruptedException {

        TransactionReceipt transactionReceipt =
        miniContract.mint(
                iClassUtil.StringToBytesInUTF8(A),
                iClassUtil.StringToBytesInUTF8(B),
                iClassUtil.StringToBytesInUTF8(Aphone),
                iClassUtil.StringToBytesInUTF8(Bphone),
                iClassUtil.StringToBytesInUTF8(tilte),
                iClassUtil.StringToBytesInUTF8(content),
                iClassUtil.StringToBytesInUTF8(sound),
                iClassUtil.StringToBytesInUTF8(picture)
        ).sendAsync().get();
        return transactionReceipt.getTransactionHash();
    }

    public long checkMinded(String transactionHash) throws Exception {
        TransactionReceipt transactionReceipt =web3j.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt().orElse(null);
        if(transactionReceipt == null){
            throw new Exception("transactionHash isn't found");
        }

        return  miniContract.getEMintEvents(transactionReceipt).get(0).dbindex.longValue();
    }

}
