package cn.iclass.udap.minicontract;

import cn.iclass.udap.minicontract.smartContract.MiniContract;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class Test {
    public static Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/aTFmVjYLRjSaUoS2xGeD"));
    public static Credentials credentials = Credentials.create("79972a6328ab7fd6bc90cae4b72fa41e9ad6ee376f39c0ebd5c4926157f1e97f");
    public static MiniContract miniContract = MiniContract.load("7d6fe5e2ad3f4b980f56727a9651f1b0efcfd856",web3j,credentials, BigInteger.valueOf(14000000000l), BigInteger.valueOf(7000000l));
    public static String contractAddress = "7d6fe5e2ad3f4b980f56727a9651f1b0efcfd856";
    public static void main(String[] args) throws Exception {
        String txHash = mint("lyc","wwl","15757813354","who knows","title","content","sound","picture");

        long dbindex = checkMinded(txHash);

        return;
    }

    public static String mint(String A, String B, String Aphone, String Bphone, String tilte, String content, String sound, String picture) throws ExecutionException, InterruptedException {
        byte[] _A = StringToBytesInUTF8(A);
        byte[] _B = StringToBytesInUTF8(B);
        byte[] _Aphone = StringToBytesInUTF8(Aphone);
        byte[] _Bphone = StringToBytesInUTF8(Bphone);
        byte[] _title = StringToBytesInUTF8(tilte);
        byte[] _content = StringToBytesInUTF8(content);
        byte[] _sound = StringToBytesInUTF8(sound);
        byte[] _picture = StringToBytesInUTF8(picture);

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

    public static long checkMinded(String transactionHash) throws Exception {
        TransactionReceipt transactionReceipt =web3j.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt().orElse(null);
        if(transactionReceipt == null){
            return -1;
        }

        return  miniContract.getEMintEvents(transactionReceipt).get(0).dbindex.longValue();
    }

    public static byte[] StringToBytesInUTF8(String str){
        return str.getBytes(Charset.forName("UTF-8"));
    }

    public static String BytesInUTF8ToString(byte[] b){
        return new String(b,Charset.forName("UTF-8"));
    }
}
