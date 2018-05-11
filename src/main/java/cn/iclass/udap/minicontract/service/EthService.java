package cn.iclass.udap.minicontract.service;

import cn.iclass.udap.minicontract.smartContract.MiniContract;
import cn.iclass.udap.minicontract.util.IClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Service
public class EthService {

    @Autowired
    public MiniContract miniContract;

    @Autowired
    public IClassUtil iClassUtil;

    @Autowired
    public Web3j web3j;

    @Autowired
    public Credentials credentials;

    @Value("${miniContract.ethereumAddress}")
    public String contractAddress;

    public String mintSync(String A, String B, String Aphone, String Bphone, String tilte, String content, String sound, String picture) throws ExecutionException, InterruptedException {

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

    public String mint(String A, String B, String Aphone, String Bphone, String tilte, String content, String sound, String picture) throws ExecutionException, InterruptedException {
        byte[] _A = iClassUtil.StringToBytesInUTF8(A);
        byte[] _B = iClassUtil.StringToBytesInUTF8(B);
        byte[] _Aphone = iClassUtil.StringToBytesInUTF8(Aphone);
        byte[] _Bphone = iClassUtil.StringToBytesInUTF8(Bphone);
        byte[] _title = iClassUtil.StringToBytesInUTF8(tilte);
        byte[] _content = iClassUtil.StringToBytesInUTF8(content);
        byte[] _sound = iClassUtil.StringToBytesInUTF8(sound);
        byte[] _picture = iClassUtil.StringToBytesInUTF8(picture);

        Function function = new Function(
                "mint",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(_A),
                        new org.web3j.abi.datatypes.DynamicBytes(_B),
                        new org.web3j.abi.datatypes.DynamicBytes(_Aphone),
                        new org.web3j.abi.datatypes.DynamicBytes(_Bphone),
                        new org.web3j.abi.datatypes.DynamicBytes(_title),
                        new org.web3j.abi.datatypes.DynamicBytes(_content),
                        new org.web3j.abi.datatypes.DynamicBytes(_sound),
                        new org.web3j.abi.datatypes.DynamicBytes(_picture)),
                Collections.<TypeReference<?>>emptyList());
        String encodedFunction = FunctionEncoder.encode(function);
        //System.out.println("encodedFunction = " + encodedFunction);

        BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING).sendAsync().get().getTransactionCount();

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,//0
                BigInteger.valueOf(14000000000l),//price
                BigInteger.valueOf(7000000l),//limit//0x989680
                contractAddress,//"0xefa4a761228d781264bf228a15195c532832e4fa",
                BigInteger.ZERO,//BigInteger.ONE,//value
                encodedFunction);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String encodedRawTransaction = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(encodedRawTransaction).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        return transactionHash;
    }

    public long checkMinded(String transactionHash) throws Exception {
        TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt().orElse(null);
        if (transactionReceipt == null) {
            throw new Exception("transactionHash isn't found");
        }

        return miniContract.getEMintEvents(transactionReceipt).get(0).dbindex.longValue();
    }

}
